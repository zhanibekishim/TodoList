package com.jax.todolist.domain.usecase

import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository

class GetTodoItemUseCase(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(todoItemId: Int): TodoItem {
        return todoRepository.getTodoItem(todoItemId)
    }
}