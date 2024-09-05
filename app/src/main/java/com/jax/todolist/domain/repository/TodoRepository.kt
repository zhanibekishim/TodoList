package com.jax.todolist.domain.repository

import androidx.lifecycle.LiveData
import com.jax.todolist.domain.entity.TodoItem

interface TodoRepository {

    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun removeTodoItem(todoItem: TodoItem)
    suspend fun editTodoItem(todoItem: TodoItem)
    suspend fun getTodoItem(todoItemId: Int): TodoItem
    fun getTodoList(): LiveData<List<TodoItem>>
}