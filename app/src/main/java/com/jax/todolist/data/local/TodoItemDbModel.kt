package com.jax.todolist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jax.todolist.domain.entity.Level

@Entity(tableName = "todo_items")
class TodoItemDbModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val title: String,
    val description: String,
    val enabled: Boolean,
    val level: Level,
)
