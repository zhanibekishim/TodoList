package com.jax.todolist.domain.repository

import com.jax.todolist.domain.entity.TodoItem

interface TodoRepository {

    fun addTodoItem(todoItem: TodoItem)
    fun removeTodoItem(todoItem: TodoItem)
    fun editTodoItem(todoItem: TodoItem)
    fun getTodoItem(todoItemId: Int): TodoItem
    fun getTodoList(): List<TodoItem>
}