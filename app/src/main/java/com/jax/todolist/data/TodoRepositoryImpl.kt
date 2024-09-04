package com.jax.todolist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jax.todolist.domain.entity.Level
import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository
import kotlin.random.Random

class TodoRepositoryImpl : TodoRepository {

    private val todoListLD = MutableLiveData<List<TodoItem>>()
    private val todoList = sortedSetOf<TodoItem>({ o1, o2 -> o1.id.compareTo(o2.id) })
    private var autoIncrementId = 0

    init {
        for (i in 0 until 10) {
            val todoItem = TodoItem(
                id = autoIncrementId++,
                title = "Name $i",
                description = "Description $i",
                level = Level.LOW,
                enabled = Random.nextBoolean()
            )
            todoList.add(todoItem)
        }
        todoListLD.value = todoList.toList()
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