package com.jax.todolist.di

import android.app.Application
import com.jax.todolist.presentation.detail.TodoItemFragment
import com.jax.todolist.presentation.todolist.TodoListActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [DataModule::class,ViewModelModule::class]
)
interface ApplicationComponent {

    fun inject(todoListActivity: TodoListActivity)
    fun inject(todoItemFragment: TodoItemFragment)

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance application: Application
        ):ApplicationComponent
    }
}