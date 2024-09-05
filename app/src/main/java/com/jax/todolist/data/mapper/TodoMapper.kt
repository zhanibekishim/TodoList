package com.jax.todolist.data.mapper

import com.jax.todolist.data.local.TodoItemDbModel
import com.jax.todolist.domain.entity.TodoItem
import javax.inject.Inject

class TodoMapper @Inject constructor(){

    fun dbModelToEntity(todoItemDbModel: TodoItemDbModel) = TodoItem(
        id = todoItemDbModel.id,
        title = todoItemDbModel.title,
        description = todoItemDbModel.description,
        enabled = todoItemDbModel.enabled,
        level = todoItemDbModel.level
    )

    fun entityToDbModel(todoItem: TodoItem) = TodoItemDbModel(
        id = todoItem.id,
        title = todoItem.title,
        description = todoItem.description,
        enabled = todoItem.enabled,
        level = todoItem.level
    )

    fun dbModelListToEntityList(todoItemDbModelList: List<TodoItemDbModel>) = todoItemDbModelList.map {
        dbModelToEntity(it)
    }
}