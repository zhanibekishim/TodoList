package com.jax.todolist.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jax.todolist.R
import com.jax.todolist.databinding.ActivityMainBinding
import com.jax.todolist.presentation.adapter.TodoAdapter
import com.jax.todolist.presentation.adapter.TodoAdapter.Companion.MAX_POOL_SIZE


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
        setUpRecyclerView()
        observeViewModel()
        setUpClickListeners()
    }

    private fun setUpRecyclerView() {
        with(binding.rvTodoListItems) {
            todoAdapter = TodoAdapter()
            adapter = todoAdapter
            recycledViewPool.setMaxRecycledViews(R.layout.todo_item, MAX_POOL_SIZE)
        }
    }

    private fun observeViewModel() {
        viewModel.todoList.observe(this) { todoList ->
            todoAdapter.submitList(todoList)
        }
    }

    private fun setUpClickListeners() {
        todoAdapter.onTodoItemClickListener = {

        }
        todoAdapter.onTodoItemLongClickListener = {
            viewModel.changeEnabledState(it)
        }
    }
}