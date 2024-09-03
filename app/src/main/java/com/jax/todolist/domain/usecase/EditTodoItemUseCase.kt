package com.jax.todolist.domain.usecase

import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository

class EditTodoItemUseCase(
    private val repository: TodoRepository
){
    fun editTodoItem(todoItem: TodoItem) {
        repository.editTodoItem(todoItem)
    }
}