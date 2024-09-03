package com.jax.todolist.domain.usecase

import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository

class GetTodoItemUseCase(
    private val todoRepository: TodoRepository
) {
    fun getTodoItem(todoItemId: Int): TodoItem {
        return todoRepository.getTodoItem(todoItemId)
    }
}