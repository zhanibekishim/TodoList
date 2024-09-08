package com.jax.todolist.presentation

import android.app.Application
import android.net.Uri
import android.util.Log
import com.jax.todolist.di.ApplicationScope
import com.jax.todolist.di.DaggerApplicationComponent
import com.jax.todolist.domain.entity.Level
import com.jax.todolist.domain.entity.TodoItem
import kotlin.concurrent.thread

@ApplicationScope
class TodoApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()

        thread {
            val cursorTodoList = contentResolver.query(
                Uri.parse("content://com.jax.todolist/todolist"),
                null,
                null,
                null,
                null
            )

            cursorTodoList?.use { cursor ->
                while (cursor.moveToNext()) {
                    val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                    val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                    val enabled = cursor.getInt(cursor.getColumnIndexOrThrow("enabled")) > 0
                    val levelId = cursor.getInt(cursor.getColumnIndexOrThrow("level"))
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))


                    val level = when (levelId) {
                        1 -> Level.LOW
                        2 -> Level.MEDIUM
                        3 -> Level.HIGH
                        else -> Level.UNKNOWN
                    }

                    val todoItem = TodoItem(
                        title = title,
                        description = description,
                        enabled = enabled,
                        id = id,
                        level = level
                    )
                    Log.d("fetched data from db", todoItem.toString())
                }
            }
        }

    }
}
