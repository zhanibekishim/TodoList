package com.jax.todolist.presentation.todolist

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jax.todolist.R
import com.jax.todolist.databinding.ActivityMainBinding
import com.jax.todolist.presentation.adapter.TodoAdapter
import com.jax.todolist.presentation.adapter.TodoAdapter.Companion.MAX_POOL_SIZE
import com.jax.todolist.presentation.detail.TodoItemActivity
import com.jax.todolist.presentation.detail.TodoItemFragment


class TodoListActivity : AppCompatActivity(),TodoItemFragment.OnEditingFinishedEvent {

    private val viewModel by lazy {
        ViewModelProvider(this)[TodoListViewModel::class.java]
    }
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var todoAdapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpRecyclerView(this)
        observeViewModel()
        setUpFabAddClickListener(this)
    }

    private fun isOnePaneMode() = binding.fragmentContainerView == null
    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view,fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun setUpRecyclerView(context: Context) {
        with(binding.rvTodoListItems) {
            todoAdapter = TodoAdapter()
            adapter = todoAdapter
            recycledViewPool.setMaxRecycledViews(R.layout.todo_item, MAX_POOL_SIZE)
            onTodoItemTouchHelper()
            onTodoItemLongClickListener()
            onTodoItemClickListener(context)
        }
    }

    private fun onTodoItemLongClickListener() {
        todoAdapter.onTodoItemLongClickListener = {todoItem->
            viewModel.changeEnabledState(todoItem)
        }
    }

    private fun onTodoItemClickListener(context: Context) {
        todoAdapter.onTodoItemClickListener = { todoItem ->
            if(isOnePaneMode()){
                val intent = TodoItemActivity.newIntentEditItem(context, todoItem.id)
                startActivity(intent)
            }else{
                launchFragment(TodoItemFragment.newInstanceEdit(todoItem.id))
            }
        }
    }

    private fun setUpFabAddClickListener(context: Context) {
        binding.fbAddItem.setOnClickListener {todoItem ->
            if(isOnePaneMode()){
                val intent = TodoItemActivity.newIntentAddItem(context)
                startActivity(intent)
            }else{
                launchFragment(TodoItemFragment.newInstanceEdit(todoItem.id))
            }
        }
    }

    private fun onTodoItemTouchHelper() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = todoAdapter.currentList[viewHolder.adapterPosition]
                viewModel.removeTodoItem(item)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvTodoListItems)
    }

    private fun observeViewModel() {
        viewModel.todoList.observe(this) { todoList ->
            todoAdapter.submitList(todoList)
        }
    }

    override fun onEditingFinished() {
        finish()
        Toast.makeText(this, "Saved succesfully", Toast.LENGTH_SHORT).show()
    }
}