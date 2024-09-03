package com.jax.todolist.domain.usecase

import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository

class AddTodoItemUseCase(
    private val repository: TodoRepository
){
    fun addTodoItem(todoItem: TodoItem) {
        repository.addTodoItem(todoItem)
    }
}