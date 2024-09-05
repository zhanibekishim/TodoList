package com.jax.todolist.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoItem(
    val title: String,
    val description: String,
    val enabled: Boolean,
    val level: Level,
    var id: Int = UNDEFINED_ID
): Parcelable {
    companion object{
        const val UNDEFINED_ID = 0
    }
}