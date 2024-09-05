package com.jax.todolist.domain.usecase

import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository
import javax.inject.Inject

class EditTodoItemUseCase @Inject constructor(
    private val repository: TodoRepository
){
    suspend operator fun invoke(todoItem: TodoItem) {
        repository.editTodoItem(todoItem)
    }
}