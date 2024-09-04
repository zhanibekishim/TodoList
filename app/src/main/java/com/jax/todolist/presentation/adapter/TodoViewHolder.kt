package com.jax.todolist.presentation.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jax.todolist.R
import com.jax.todolist.databinding.TodoItemBinding
import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.presentation.adapter.TodoAdapter.Companion.DISABLED_HIGH
import com.jax.todolist.presentation.adapter.TodoAdapter.Companion.DISABLED_LOW
import com.jax.todolist.presentation.adapter.TodoAdapter.Companion.DISABLED_MID
import com.jax.todolist.presentation.adapter.TodoAdapter.Companion.ENABLED_HIGH
import com.jax.todolist.presentation.adapter.TodoAdapter.Companion.ENABLED_LOW
import com.jax.todolist.presentation.adapter.TodoAdapter.Companion.ENABLED_MID

class TodoViewHolder(private val binding: TodoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(todoItem: TodoItem, viewType: Int) {
        binding.tvTitle.text = todoItem.title
        binding.tvDescription.text = todoItem.description
        val background = when (viewType) {
            ENABLED_LOW -> {
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.low_enabled_todo_item_bg
                )
            }

            ENABLED_MID -> {
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.mid_enabled_todo_item_bg
                )
            }

            ENABLED_HIGH -> {
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.high_enabled_todo_item_bg
                )
            }

            DISABLED_LOW -> {
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.low_disabled_todo_item_bg
                )
            }

            DISABLED_MID -> {
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.mid_disabled_todo_item_bg
                )
            }

            DISABLED_HIGH -> {
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.high_disabled_todo_item_bg
                )
            }

            else -> throw RuntimeException()
        }
        binding.llCard.background = background
    }
}