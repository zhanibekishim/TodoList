package com.jax.todolist.di

import androidx.lifecycle.ViewModel
import com.jax.todolist.presentation.detail.TodoItemViewModel
import com.jax.todolist.presentation.todolist.TodoListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds @IntoMap @ViewModelKey(TodoListViewModel::class)
    fun bindsTodoListViewModel(viewModel: TodoListViewModel):ViewModel

    @Binds @IntoMap @ViewModelKey(TodoItemViewModel::class)
    fun bindsTodoItemViewModel(viewModel: TodoItemViewModel):ViewModel
}