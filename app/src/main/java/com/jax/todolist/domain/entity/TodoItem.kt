package com.jax.todolist.domain.entity

data class TodoItem(
    val title: String,
    val description: String,
    val enabled: Boolean,
    val level: Level,
    var id: Int = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = -1
    }
}