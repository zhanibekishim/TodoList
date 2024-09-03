package com.jax.todolist.domain.usecase

import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository

class GetTodoListUseCase(
    private val todoListRepository: TodoRepository
) {
    fun getTodoList(): List<TodoItem> {
        return todoListRepository.getTodoList()
    }
}