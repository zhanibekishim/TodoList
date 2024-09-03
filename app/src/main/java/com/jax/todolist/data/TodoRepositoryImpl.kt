package com.jax.todolist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository

class TodoRepositoryImpl : TodoRepository {

    private val todoListLD = MutableLiveData<List<TodoItem>>()
    private val todoList = mutableListOf<TodoItem>()
    private var autoIncrementId = 0

    init {
        for (i in 0 until 10) {
            val todoItem = TodoItem(
                id = autoIncrementId++,
                name = "Name $i",
                description = "Description $i",
                enabled = true
            )
            todoList.add(todoItem)
        }
    }

    override fun addTodoItem(todoItem: TodoItem) {
        if (todoItem.id == TodoItem.UNDEFINED_ID) {
            autoIncrementId++
            todoItem.id = autoIncrementId
        }
        todoList.add(todoItem)
        updateTodoList()
    }


    override fun removeTodoItem(todoItem: TodoItem) {
        todoList.remove(todoItem)
        updateTodoList()
    }

    override fun editTodoItem(todoItem: TodoItem) {
        val oldItem = getTodoItem(todoItem.id)
        todoList.remove(oldItem)
        addTodoItem(todoItem)
    }

    override fun getTodoItem(todoItemId: Int): TodoItem {
        return todoList.find { todoItem ->
            todoItem.id == todoItemId
        } ?: throw RuntimeException("TodoItem with id $todoItemId not found")
    }

    override fun getTodoList(): LiveData<List<TodoItem>> {
        return todoListLD
    }
    private fun updateTodoList() {
        todoListLD.value = todoList.toList()
    }
}