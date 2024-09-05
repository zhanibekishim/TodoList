package com.jax.todolist.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoItemDbModel::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: TodoDatabase? = null
        private val LOCK = Any()
        private val DB_NAME = "todo_item.db"
        fun getInstance(application: Application): TodoDatabase {
            INSTANCE?.let { return it }
            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val db = Room.databaseBuilder(
                    application,
                    TodoDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
    abstract fun todoItemDao(): TodoItemDao
}