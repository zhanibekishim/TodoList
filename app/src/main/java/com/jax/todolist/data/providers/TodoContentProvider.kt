package com.jax.todolist.data.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.jax.todolist.data.local.TodoItemDao
import com.jax.todolist.data.local.TodoItemDbModel
import com.jax.todolist.data.mapper.TodoMapper
import com.jax.todolist.domain.entity.Level
import com.jax.todolist.presentation.TodoApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoContentProvider : ContentProvider() {

    @Inject
    lateinit var dao: TodoItemDao

    @Inject
    lateinit var mapper: TodoMapper

    private val component by lazy {
        (context as TodoApp).component
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, "todolist", TODO_LIST_QUERY1)
        addURI(AUTHORITY, "todolist/*", TODO_LIST_QUERY2)
        addURI(AUTHORITY, "todolist/#", TODO_LIST_QUERY3)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            TODO_LIST_QUERY1 -> {
                return dao.getTodoListCursor()
            }

            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            TODO_LIST_QUERY1 -> {
                if (values == null) return null
                val id = values.getAsInteger("id")
                val title = values.getAsString("title")
                val description = values.getAsString("description")
                val enabled = values.getAsBoolean("enabled")
                val levelId = values.getAsInteger("level")

                val level = when (levelId) {
                    1 -> Level.LOW
                    2 -> Level.MEDIUM
                    3 -> Level.HIGH
                    else -> Level.UNKNOWN
                }

                val todoItemDbModel = TodoItemDbModel(
                    id = id,
                    title = title,
                    description = description,
                    enabled = enabled,
                    level = level
                )

                CoroutineScope(Dispatchers.IO).launch {
                    dao.addTodoItem(todoItemDbModel)
                }
            }
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when (uriMatcher.match(uri)) {
            TODO_LIST_QUERY1 -> {
                val id = selectionArgs?.get(0) ?: return 0
                CoroutineScope(Dispatchers.IO).launch {
                    dao.deleteItemContentResolver(id.toInt())
                }
            }
        }
        return NOTHING_CHANGED_IN_DB
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        when (uriMatcher.match(uri)) {
            TODO_LIST_QUERY1 -> {
                if (values == null) return NOTHING_CHANGED_IN_DB
                val title = values.getAsString("title")
                val description = values.getAsString("description")
                val enabled = values.getAsBoolean("enabled")

                val levelId = values.getAsInteger("level")
                val level = when (levelId) {
                    1 -> Level.LOW
                    2 -> Level.MEDIUM
                    3 -> Level.HIGH
                    else -> Level.UNKNOWN
                }

                val todoItemId = selectionArgs?.get(INDEX_OF_ID) ?: return 0

                CoroutineScope(Dispatchers.IO).launch {
                    dao.addTodoItem(
                        TodoItemDbModel(
                            id = todoItemId.toInt(),
                            title = title,
                            description = description,
                            enabled = enabled,
                            level = level
                        )
                    )
                }
            }
        }
        return NOTHING_CHANGED_IN_DB
    }

    companion object {
        private const val TODO_LIST_QUERY1 = 100
        private const val TODO_LIST_QUERY2 = 101
        private const val TODO_LIST_QUERY3 = 102

        private const val AUTHORITY = "com.jax.todolist"

        private const val ID_EXTRA = "id"
        private const val TITLE_EXTRA = "title"
        private const val DESCRIPTION_EXTRA = "description"
        private const val ENABLED_EXTRA = "enabled"
        private const val LEVEL_EXTRA = "level"

        private const val INDEX_OF_ID = 0
        private const val NOTHING_CHANGED_IN_DB = 0
        fun contentValuesToTodoItem(
            id: Int,
            title: String,
            description: String,
            enabled: Boolean = true,
            levelString: String
        ): ContentValues {
            val level = Level.valueOf(levelString)
            return ContentValues().apply {
                put(ID_EXTRA, id)
                put(TITLE_EXTRA, title)
                put(DESCRIPTION_EXTRA, description)
                put(ENABLED_EXTRA, enabled)
                put(LEVEL_EXTRA, level.ordinal)
            }
        }

        fun selectionArgs(id: Int): Array<String> {
            return arrayOf(id.toString())
        }
    }
}
