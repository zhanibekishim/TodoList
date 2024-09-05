package com.jax.todolist.domain.usecase

import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository
import javax.inject.Inject

class RemoveTodoItemUseCase @Inject constructor(
    private val todoListRepository: TodoRepository
) {
    suspend operator fun invoke(todoItem: TodoItem) {
        todoListRepository.removeTodoItem(todoItem)
    }
}