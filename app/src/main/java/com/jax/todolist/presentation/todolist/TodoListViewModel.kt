package com.jax.todolist.presentation.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.usecase.EditTodoItemUseCase
import com.jax.todolist.domain.usecase.GetTodoListUseCase
import com.jax.todolist.domain.usecase.RemoveTodoItemUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoListViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val removeTodoItemUseCase: RemoveTodoItemUseCase,
) : ViewModel() {

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