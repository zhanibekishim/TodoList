package com.jax.todolist.data.repository

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.jax.todolist.data.local.TodoDatabase
import com.jax.todolist.data.local.TodoItemDbModel
import com.jax.todolist.data.mapper.TodoMapper
import com.jax.todolist.domain.entity.Level
import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    application: Application
) : TodoRepository {

    private val dao = TodoDatabase.getInstance(application).todoItemDao()
    private val mapper = TodoMapper()

    init {
        val baseNote = TodoItemDbModel(
            id = 0,
            title = "My dream and goals",
            description = "I wanna become a great person",
            enabled = true,
            level = Level.HIGH
        )
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch{
            dao.addTodoItem(baseNote)
        }
    }
    override suspend fun addTodoItem(todoItem: TodoItem) {
        dao.addTodoItem(mapper.entityToDbModel(todoItem))
    }

    override suspend fun removeTodoItem(todoItem: TodoItem) {
        dao.deleteTodoItem(todoItem.id)
    }

    override suspend fun editTodoItem(todoItem: TodoItem) {
        dao.addTodoItem(mapper.entityToDbModel(todoItem))
    }

    override suspend fun getTodoItem(todoItemId: Int): TodoItem {
        return mapper.dbModelToEntity(dao.getTodoItem(todoItemId))
    }

    override fun getTodoList() = MediatorLiveData<List<TodoItem>>().apply {
        addSource(dao.getTodoList()) {listDbModel->
            value = mapper.dbModelListToEntityList(listDbModel)
        }
    }
}