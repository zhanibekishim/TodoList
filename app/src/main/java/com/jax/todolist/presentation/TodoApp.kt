package com.jax.todolist.presentation

import android.app.Application
import com.jax.todolist.di.ApplicationScope
import com.jax.todolist.di.DaggerApplicationComponent

@ApplicationScope
class TodoApp : Application() {
    val component by lazy{
        DaggerApplicationComponent.factory().create(this)
    }
}