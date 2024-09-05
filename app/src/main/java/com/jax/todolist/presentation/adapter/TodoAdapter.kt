package com.jax.todolist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jax.todolist.databinding.HighDisabledTodoItemBinding
import com.jax.todolist.databinding.HighEnabledTodoItemBinding
import com.jax.todolist.databinding.LowDisabledTodoItemBinding
import com.jax.todolist.databinding.LowEnabledTodoItemBinding
import com.jax.todolist.databinding.MidDisabledTodoItemBinding
import com.jax.todolist.databinding.MidEnabledTodoItemBinding
import com.jax.todolist.domain.entity.Level
import com.jax.todolist.domain.entity.TodoItem


class TodoAdapter : ListAdapter<TodoItem, RecyclerView.ViewHolder>(TodoDiffCallBack()) {

    var onTodoItemLongClickListener: ((TodoItem) -> Unit)? = null
    var onTodoItemClickListener: ((TodoItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = when (viewType) {
            ENABLED_LOW -> {
                val binding = LowEnabledTodoItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TodoLowEnabledViewHolder(binding)
            }

            DISABLED_LOW -> {
                val binding = LowDisabledTodoItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TodoLowDisabledViewHolder(binding)
            }

            ENABLED_MID -> {
                val binding = MidEnabledTodoItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TodoMidEnabledViewHolder(binding)
            }

            DISABLED_MID -> {
                val binding = MidDisabledTodoItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TodoMidDisabledViewHolder(binding)
            }

            ENABLED_HIGH -> {
                val binding = HighEnabledTodoItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TodoHighEnabledViewHolder(binding)
            }

            DISABLED_HIGH -> {
                val binding = HighDisabledTodoItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TodoHighDisabledViewHolder(binding)
            }

            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val todoItem = getItem(position)
        val viewType = getItemViewType(position)
        when(viewType){
            ENABLED_LOW -> (holder as TodoLowEnabledViewHolder).bind(todoItem)
            ENABLED_MID -> (holder as TodoMidEnabledViewHolder).bind(todoItem)
            ENABLED_HIGH -> (holder as TodoHighEnabledViewHolder).bind(todoItem)
            DISABLED_LOW -> (holder as TodoLowDisabledViewHolder).bind(todoItem)
            DISABLED_MID -> (holder as TodoMidDisabledViewHolder).bind(todoItem)
            DISABLED_HIGH -> (holder as TodoHighDisabledViewHolder).bind(todoItem)
        }
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
