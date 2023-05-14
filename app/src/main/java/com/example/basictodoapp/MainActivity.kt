package com.example.basictodoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.basictodoapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    private fun sampleTodoList(): MutableList<Todo> {
        return mutableListOf(
            Todo("Buy milk", false),
            Todo("Buy eggs", false),
            Todo("Buy bread", false),
            Todo("Buy cheese", false),
            Todo("Buy butter", false),
            Todo("Buy yogurt", false),
            Todo("Buy meat", false),
            Todo("Buy fish", false)
        )
    }

    private fun setupRecyclerView() {
        val todoAdapter = TodoAdapter(sampleTodoList())
        binding.rvTodoList.apply {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAnimator = null
        }
        setupBtnAdd(todoAdapter)
        setupSwipeToDelete(todoAdapter)
    }

    private fun setupBtnAdd(todoAdapter: TodoAdapter) {

        binding.btnAdd.setOnClickListener {
            val todoTitle = binding.etAddTodo.text.toString()
            if (todoTitle.isNotEmpty()) {
                todoAdapter.todoList.add(Todo(todoTitle,false))
                todoAdapter.notifyItemInserted(todoAdapter.todoList.size - 1)
                binding.etAddTodo.text?.clear()

                Snackbar.make(
                    binding.root, "An item is added", Snackbar.LENGTH_LONG
                )
                    .setAction("Undo") {
                        todoAdapter.todoList.remove(Todo(todoTitle))
                        todoAdapter.notifyItemInserted(todoAdapter.todoList.size - 1)
                    }
                    .show()
            }
            else {
                Snackbar.make(
                    binding.root, "Please input the item name", Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupSwipeToDelete(todoAdapter: TodoAdapter) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target : ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removeItem = todoAdapter.todoList.removeAt(position)
                todoAdapter.notifyItemInserted(position)

                Snackbar.make(
                    binding.root, "An item is removed", Snackbar.LENGTH_LONG
                )
                    .setAction("Undo") {
                        todoAdapter.todoList.add(position, removeItem)
                        todoAdapter.notifyItemInserted(position)
                    }
                    .show()
            }
        }).attachToRecyclerView(binding.rvTodoList)
    }
}