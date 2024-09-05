package com.jax.todolist.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.jax.todolist.databinding.MidDisabledTodoItemBinding
import com.jax.todolist.domain.entity.TodoItem

class TodoMidDisabledViewHolder(private val binding: MidDisabledTodoItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(todoItem: TodoItem){
        binding.tvTitle.text = todoItem.title
        binding.tvDescription.text = todoItem.description
    }
}