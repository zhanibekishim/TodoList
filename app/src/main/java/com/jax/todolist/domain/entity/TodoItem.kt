package com.jax.todolist.domain.entity

data class TodoItem(
    val id: Int,
    val name: String,
    val description: String,
    val enabled: Boolean,
)