package com.jax.todolist.presentation.main

import androidx.lifecycle.ViewModel
import com.jax.todolist.data.TodoRepositoryImpl
import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.usecase.EditTodoItemUseCase
import com.jax.todolist.domain.usecase.GetTodoListUseCase
import com.jax.todolist.domain.usecase.RemoveTodoItemUseCase

class MainViewModel:ViewModel() {

    private val repository = TodoRepositoryImpl()

    private val getTodoListUseCase = GetTodoListUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)
    private val removeTodoItemUseCase = RemoveTodoItemUseCase(repository)

    val todoList = getTodoListUseCase()

    fun changeEnabledState(todoItem: TodoItem){
        val newItem = todoItem.copy(enabled = !todoItem.enabled)
        editTodoItemUseCase(newItem)
    }
    fun removeTodoItem(todoItem: TodoItem){
        removeTodoItemUseCase(todoItem)
    }
}