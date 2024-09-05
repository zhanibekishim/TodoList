package com.jax.todolist.domain.usecase

import androidx.lifecycle.LiveData
import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodoListUseCase @Inject constructor(
    private val todoListRepository: TodoRepository
) {
    operator fun invoke(): LiveData<List<TodoItem>> {
        return todoListRepository.getTodoList()
    }
}