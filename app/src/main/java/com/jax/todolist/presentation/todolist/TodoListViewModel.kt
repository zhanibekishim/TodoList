package com.jax.todolist.presentation.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jax.todolist.data.repository.TodoRepositoryImpl
import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.usecase.EditTodoItemUseCase
import com.jax.todolist.domain.usecase.GetTodoListUseCase
import com.jax.todolist.domain.usecase.RemoveTodoItemUseCase
import kotlinx.coroutines.launch

class TodoListViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = TodoRepositoryImpl(application)

    private val getTodoListUseCase = GetTodoListUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)
    private val removeTodoItemUseCase = RemoveTodoItemUseCase(repository)

    val todoList = getTodoListUseCase()

    fun changeEnabledState(todoItem: TodoItem) {
        viewModelScope.launch {
            val newItem = todoItem.copy(enabled = !todoItem.enabled)
            editTodoItemUseCase(newItem)
        }
    }

    fun removeTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            removeTodoItemUseCase(todoItem)
        }
    }
}