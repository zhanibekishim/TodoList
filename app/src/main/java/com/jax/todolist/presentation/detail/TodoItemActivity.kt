package com.jax.todolist.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jax.todolist.R
import com.jax.todolist.databinding.ActivityTodoItemBinding
import com.jax.todolist.domain.entity.Level
import com.jax.todolist.domain.entity.TodoItem

class TodoItemActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[TodoItemViewModel::class.java]
    }
    private val binding by lazy {
        ActivityTodoItemBinding.inflate(layoutInflater)
    }

    private var screen_mode = UNKNOWN_MODE
    private var todoItemId = TodoItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        parseIntent()
        setUpLevelListView()
        launchRightMode()
        observerViewModel()
        onTextChangedListeners()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Unknown screen mode")
        }
        screen_mode = intent.getStringExtra(EXTRA_SCREEN_MODE).toString()

        if (screen_mode != MODE_ADD && screen_mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode")
        }
        if (screen_mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_TODO_ITEM_ID)) {
                throw RuntimeException("Unknown todo item id absent")
            }
            todoItemId = intent.getIntExtra(EXTRA_TODO_ITEM_ID, TodoItem.UNDEFINED_ID)
        }
    }

    private fun setUpLevelListView(){
        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.level_item,
            Level.entries.toTypedArray().sliceArray(1..3)
        )
        binding.spinnerLevel.adapter = arrayAdapter
    }

    private fun launchRightMode() {
        when (screen_mode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
            viewModel.addTodoItem(
                binding.etTitle.text.toString(),
                binding.etDescription.text.toString(),
                binding.spinnerLevel.selectedItem.toString()
            )
        }
    }

    private fun launchEditMode() {
        viewModel.getTodoItem(todoItemId)
        viewModel.todoItem.observe(this) { todoItem ->
            with(binding){
                etTitle.setText(todoItem.title)
                etDescription.setText(todoItem.description)
            }
        }
        binding.saveButton.setOnClickListener {
            viewModel.editTodoItem(
                binding.etTitle.text.toString(),
                binding.etDescription.text.toString(),
                binding.spinnerLevel.selectedItem.toString()
            )
        }
    }

    private fun observerViewModel() {
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
        viewModel.errorTitle.observe(this) { isError ->
            val message = if (isError) {
                getString(R.string.error_input_title)
            } else {
                null
            }
            binding.tilTitle.error = message
        }
        viewModel.errorDescription.observe(this) { isError ->
            val message = if (isError) {
                getString(R.string.error_input_description)
            } else {
                null
            }
            binding.tilDescription.error = message
        }
    }

    private fun onTextChangedListeners() {
        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.resetErrorTitle()
            }
        })
        binding.etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.resetErrorDescription()
            }
        })
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
}