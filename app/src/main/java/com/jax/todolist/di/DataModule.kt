package com.jax.todolist.di

import android.app.Application
import com.jax.todolist.data.local.TodoDatabase
import com.jax.todolist.data.local.TodoItemDao
import com.jax.todolist.data.repository.TodoRepositoryImpl
import com.jax.todolist.domain.repository.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindsTodoRepository(impl: TodoRepositoryImpl): TodoRepository

    companion object {
        @ApplicationScope
        @Provides
        fun provides(
            application: Application
        ): TodoItemDao {
            return TodoDatabase.getInstance(application).todoItemDao()
        }
    }
}