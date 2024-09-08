package com.jax.todolist.data.local

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoItemDao {

    @Query("SELECT * FROM todo_items")
    fun getTodoList(): LiveData<List<TodoItemDbModel>>

    @Query("SELECT * from todo_items")
    fun getTodoListCursor(): Cursor

    @Query("SELECT * FROM todo_items WHERE id=:todoItemId LIMIT 1")
    suspend fun getTodoItem(todoItemId: Int): TodoItemDbModel

    @Query("DELETE FROM todo_items WHERE id=:todoItemId")
    suspend fun deleteTodoItem(todoItemId: Int)

    @Query("DELETE FROM todo_items WHERE id=:todoItemId")
    suspend fun deleteItemContentResolver(todoItemId: Int):Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItem(todoItemDbModel: TodoItemDbModel)

}