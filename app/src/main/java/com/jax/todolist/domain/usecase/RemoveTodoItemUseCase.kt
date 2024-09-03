package com.jax.todolist.domain.usecase

import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository

class RemoveTodoItemUseCase(
    private val todoListRepository: TodoRepository
) {
    operator fun invoke(todoItem: TodoItem) {
        todoListRepository.removeTodoItem(todoItem)
    }
}