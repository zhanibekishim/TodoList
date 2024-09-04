package com.jax.todolist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jax.todolist.databinding.TodoItemBinding
import com.jax.todolist.domain.entity.Level
import com.jax.todolist.domain.entity.TodoItem

class TodoAdapter : ListAdapter<TodoItem, TodoViewHolder>(TodoDiffCallBack()) {

    var onTodoItemLongClickListener: ((TodoItem) -> Unit)? = null
    var onTodoItemClickListener: ((TodoItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = getItem(position)
        val viewType = getItemViewType(position)
        holder.bind(todoItem, viewType)
        holder.itemView.setOnLongClickListener {
            onTodoItemLongClickListener?.invoke(todoItem)
            true
        }
        holder.itemView.setOnClickListener {
            onTodoItemClickListener?.invoke(todoItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val todoItem = getItem(position)
        val enabledOffset = if (todoItem.enabled) ENABLED_OFFSET else DISABLED_OFFSET
        return when (todoItem.level) {
            Level.LOW -> ENABLED_LOW + enabledOffset
            Level.MEDIUM -> ENABLED_MID + enabledOffset
            Level.HIGH -> ENABLED_HIGH + enabledOffset
            else -> throw RuntimeException("Unknown level: ${todoItem.level}")
        }
    }

    companion object {
        const val ENABLED_LOW = 1
        const val DISABLED_LOW = 2
        const val ENABLED_MID = 3
        const val DISABLED_MID = 4
        const val ENABLED_HIGH = 5
        const val DISABLED_HIGH = 6
        const val MAX_POOL_SIZE = 30
        private const val ENABLED_OFFSET = 0
        private const val DISABLED_OFFSET = 1
    }
}
