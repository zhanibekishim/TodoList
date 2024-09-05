package com.jax.todolist.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.jax.todolist.databinding.LowDisabledTodoItemBinding
import com.jax.todolist.domain.entity.TodoItem

class TodoLowDisabledViewHolder(private val binding: LowDisabledTodoItemBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(todoItem: TodoItem){
        binding.tvTitle.text = todoItem.title
        binding.tvDescription.text = todoItem.description
    }
}