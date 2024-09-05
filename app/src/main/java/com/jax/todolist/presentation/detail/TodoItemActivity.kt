package com.jax.todolist.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jax.todolist.R
import com.jax.todolist.databinding.ActivityTodoItemBinding
import com.jax.todolist.domain.entity.TodoItem

class TodoItemActivity: AppCompatActivity(), TodoItemFragment.OnEditingFinishedEvent {

    private val binding by lazy {
        ActivityTodoItemBinding.inflate(layoutInflater)
    }

    private var screenMode = UNKNOWN_MODE
    private var todoItemId = TodoItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        parseIntent()
        launchRightMode()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Unknown screen mode")
        }
        screenMode = intent.getStringExtra(EXTRA_SCREEN_MODE).toString()
        if (screenMode != MODE_ADD && screenMode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode")
        }
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_TODO_ITEM_ID)) {
                throw RuntimeException("Unknown todo item id absent")
            }
            todoItemId = intent.getIntExtra(EXTRA_TODO_ITEM_ID, TodoItem.UNDEFINED_ID)
        }
    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_ADD -> TodoItemFragment.newInstanceAdd()
            MODE_EDIT -> TodoItemFragment.newInstanceEdit(todoItemId)
            else ->     throw RuntimeException("Unknown screen mode")
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view,fragment)
            .commit()
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_screen_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val UNKNOWN_MODE = ""

        private const val EXTRA_TODO_ITEM_ID = "extra_shop_item_id"

        fun newIntentAddItem(context: Context): Intent {
            return Intent(context, TodoItemActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            }
        }

        fun newIntentEditItem(context: Context, todoItemId: Int): Intent {
            return Intent(context, TodoItemActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
                putExtra(EXTRA_TODO_ITEM_ID, todoItemId)
            }
        }
    }

    override fun onEditingFinished() {
        finish()
        Toast.makeText(this, "Saved succesfully", Toast.LENGTH_SHORT).show()
    }
}