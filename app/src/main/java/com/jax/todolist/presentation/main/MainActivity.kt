package com.jax.todolist.presentation.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jax.todolist.R
import com.jax.todolist.databinding.ActivityMainBinding
import com.jax.todolist.presentation.adapter.TodoAdapter
import com.jax.todolist.presentation.adapter.TodoAdapter.Companion.MAX_POOL_SIZE
import com.jax.todolist.presentation.detail.TodoItemActivity


class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
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
        onTodoItemAddClickListener()
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
        todoAdapter.onTodoItemLongClickListener = {
            viewModel.changeEnabledState(it)
        }
    }

    private fun onTodoItemClickListener(context: Context) {
        todoAdapter.onTodoItemClickListener = { todoItem ->
            val intent = TodoItemActivity.newIntentEditItem(context, todoItem.id)
            startActivity(intent)
        }
    }
    private fun onTodoItemAddClickListener() {
        binding.fbAddItem.setOnClickListener {
            val intent = TodoItemActivity.newIntentAddItem(this)
            startActivity(intent)
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
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                todoAdapter.notifyItemMoved(fromPosition, toPosition)
                return true
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
            Log.d("Adding process", "Added TodoList: $todoList")
        }
    }
}