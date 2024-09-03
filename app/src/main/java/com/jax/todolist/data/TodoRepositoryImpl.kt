package com.jax.todolist.data

import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository

class TodoRepositoryImpl: TodoRepository {

    private val todoList = mutableListOf<TodoItem>()
    private var autoIncrementId = 0

    override fun addTodoItem(todoItem: TodoItem) {
        if(todoItem.id == TodoItem.UNDEFINED_ID){
            autoIncrementId++
        }
        todoList.add(todoItem)
    }

    override fun removeTodoItem(todoItem: TodoItem) {
        todoList.remove(todoItem)
    }

    override fun editTodoItem(todoItem: TodoItem) {
        val oldItem = getTodoItem(todoItem.id)
        todoList.remove(oldItem)
        addTodoItem(todoItem)
    }

    override fun getTodoItem(todoItemId: Int): TodoItem {
        return todoList.find {todoItem->
            todoItem.id == todoItemId
        } ?: throw RuntimeException("TodoItem with id $todoItemId not found")
    }

    override fun getTodoList(): List<TodoItem> {
        return todoList.toList()
    }
}