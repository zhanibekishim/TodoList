package com.jax.todolist.domain.entity

data class TodoItem(
    val name: String,
    val description: String,
    val enabled: Boolean,
    val id: Int = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = -1
    }
}