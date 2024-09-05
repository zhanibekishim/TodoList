package com.jax.todolist.presentation.detail

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jax.todolist.R
import com.jax.todolist.databinding.FragmentTodoItemBinding
import com.jax.todolist.domain.entity.Level
import com.jax.todolist.domain.entity.TodoItem

class TodoItemFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this)[TodoItemViewModel::class.java]
    }
    private lateinit var onEditingFinishedEvent: OnEditingFinishedEvent

    private var _binding: FragmentTodoItemBinding? = null
    private val binding: FragmentTodoItemBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoItemBinding == null")


    private var screen_mode = UNKNOWN_MODE
    private var todoItemId = TodoItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEditingFinishedEvent){
            onEditingFinishedEvent = context
        }else{
            throw RuntimeException("Activity must implement OnEditingFinishedEvent")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) launchRightMode()
        setUpLevelListView()
        observerViewModel()
        onTextChangedListeners()
    }

    private fun parseIntent() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Unknown screen mode")
        }
        screen_mode = args.getString(SCREEN_MODE).toString()
        if (screen_mode != MODE_ADD && screen_mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode")
        }
        if (screen_mode == MODE_EDIT && !args.containsKey(TODO_ITEM_ID)) {
            throw RuntimeException("Unknown todo item id absent")
        }
        todoItemId = args.getInt(TODO_ITEM_ID)
    }

    private fun setUpLevelListView() {
        val levels = Level.entries.toTypedArray()
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.level_item,
            levels.map { it.toString() }
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
        viewModel.todoItem.observe(viewLifecycleOwner) { todoItem ->
            with(binding) {
                etTitle.setText(todoItem.title)
                etDescription.setText(todoItem.description)
                val levelPosition = (spinnerLevel.adapter as ArrayAdapter<String>)
                    .getPosition(todoItem.level.toString())
                spinnerLevel.setSelection(levelPosition)
            }
        }
        binding.saveButton.setOnClickListener {
            val selectedLevel = binding.spinnerLevel.selectedItem.toString()
            val level = Level.entries.find { it.toString() == selectedLevel }
            viewModel.editTodoItem(
                binding.etTitle.text.toString(),
                binding.etDescription.text.toString(),
                (level ?: Level.UNKNOWN).toString()
            )
        }
    }


    private fun observerViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedEvent.onEditingFinished()
        }
        viewModel.errorTitle.observe(viewLifecycleOwner) { isError ->
            val message = if (isError) {
                getString(R.string.error_input_title)
            } else {
                null
            }
            binding.tilTitle.error = message
        }
        viewModel.errorDescription.observe(viewLifecycleOwner) { isError ->
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

    interface OnEditingFinishedEvent {
        fun onEditingFinished()
    }

    companion object {

        private const val SCREEN_MODE = "extra_screen_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val UNKNOWN_MODE = ""

        private const val TODO_ITEM_ID = "extra_shop_item_id"

        fun newInstanceAdd(): Fragment {
            return TodoItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEdit(todoItemId: Int): Fragment {
            return TodoItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(TODO_ITEM_ID, todoItemId)
                }
            }
        }
    }
}