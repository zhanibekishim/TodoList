package com.jax.todolist.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.jax.todolist.domain.entity.TodoItem

class TodoDiffCallBack : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}
