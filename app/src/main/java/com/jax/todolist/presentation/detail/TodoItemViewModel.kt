package com.jax.todolist.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jax.todolist.data.TodoRepositoryImpl
import com.jax.todolist.domain.entity.Level
import com.jax.todolist.domain.entity.TodoItem
import com.jax.todolist.domain.usecase.AddTodoItemUseCase
import com.jax.todolist.domain.usecase.EditTodoItemUseCase
import com.jax.todolist.domain.usecase.GetTodoItemUseCase

class TodoItemViewModel : ViewModel() {

    private val repository = TodoRepositoryImpl
    private val getTodoItemUseCase = GetTodoItemUseCase(repository)
    private val addTodoItemUseCase = AddTodoItemUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)

    private val _todoItem = MutableLiveData<TodoItem>()
    val todoItem: MutableLiveData<TodoItem>
        get() = _todoItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private val _errorTitle = MutableLiveData<Boolean>()
    val errorTitle: LiveData<Boolean>
        get() = _errorTitle

    private val _errorDescription = MutableLiveData<Boolean>()
    val errorDescription: LiveData<Boolean>
        get() = _errorDescription

    fun getTodoItem(todoItemId: Int) {
        val todoItem = getTodoItemUseCase(todoItemId)
        _todoItem.value = todoItem
    }

    fun addTodoItem(title: String?, description: String?, level: String) {
        val parsedTitle = parseText(title)
        val parsedDescription = parseText(description)
        val parsedLevel = parseLevel(level)

        if (validateInput(parsedTitle, parsedDescription)) {
            val todoItem = TodoItem(
                title = parsedTitle,
                description = parsedDescription,
                level = parsedLevel,
                enabled = true
            )
            _todoItem.value = todoItem
            addTodoItemUseCase(todoItem)
            shouldCloseScreen()
        }
    }

    fun editTodoItem(title: String?, description: String?,level: String) {
        val parsedTitle = parseText(title)
        val parsedDescription = parseText(description)
        val parsedLevel = parseLevel(level)

        if (validateInput(parsedTitle, parsedDescription)) {
            val item = _todoItem.value
            item?.let { oldItem ->
                val newItem = oldItem.copy(
                    title = parsedTitle,
                    description = parsedDescription,
                    level = parsedLevel
                )
                editTodoItemUseCase(newItem)
                shouldCloseScreen()
            }
        }
    }

    private fun parseText(text: String?): String {
        return text?.trim()?.takeIf { it.isNotBlank() } ?: BLANK_TEXT
    }

    private fun parseLevel(level: String): Level {
        return when(level) {
            LEVEL_HIGH -> Level.HIGH
            LEVEL_MEDIUM -> Level.MEDIUM
            LEVEL_LOW -> Level.LOW
            else -> Level.UNKNOWN
        }
    }

    private fun validateInput(title: String, description: String): Boolean {
        var result = true
        if (title.isBlank()) {
            _errorTitle.value = true
            result = false
        }
        if (description.isBlank()) {
            _errorDescription.value = true
            result = false
        }
        return result
    }

    private fun shouldCloseScreen() {
        _shouldCloseScreen.value = Unit
    }

    fun resetErrorTitle() {
        _errorTitle.value = false
    }

    fun resetErrorDescription() {
        _errorDescription.value = false
    }

    companion object {
        private const val BLANK_TEXT = ""
        private const val LEVEL_HIGH = "HIGH"
        private const val LEVEL_LOW = "LOW"
        private const val LEVEL_MEDIUM = "MEDIUM"
    }
}