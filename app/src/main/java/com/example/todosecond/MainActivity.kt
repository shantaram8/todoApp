package com.example.todosecond

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todosecond.R.id
import com.example.todosecond.R.layout
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), ItemRowListener {

    lateinit var mDatabase: DatabaseReference
    lateinit var recyclerAdapter: ToDoItemRecyclerAdapter
    var toDoItemList: MutableList<ToDoItem>? = null
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        recyclerView = findViewById(id.items_recycler_view)
        val fab = findViewById<View>(id.fab)
        fab.setOnClickListener {
            addNewItemDialog()
        }
        mDatabase = FirebaseDatabase.getInstance().reference
        toDoItemList = mutableListOf()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = ToDoItemRecyclerAdapter(toDoItemList!!, this)
        recyclerView.adapter = recyclerAdapter

        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)


    }

    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)

        val itemEditText = EditText(this)
        alert.setMessage("Add new item")
        alert.setTitle("Enter ToDo Item Text")
        alert.setView(itemEditText)

        alert.setPositiveButton("Submit") { dialog, positiveButton ->
            if (itemEditText.text.isNotBlank()) {
                val toDoItem = ToDoItem.create()
                toDoItem.itemText = itemEditText.text.toString()
                toDoItem.done = false

                val newItem = mDatabase.child(Constants.FIREBASE_ITEM).push()
                recyclerAdapter.notifyDataSetChanged()
                toDoItem.objectId = newItem.key

                newItem.setValue(toDoItem)

                dialog.dismiss()
                recreate()
            }
        }

        alert.show()
    }

    private var itemListener: ValueEventListener = object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            addDataToList(snapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("Main Activity", "loadItem:onCancelled", error.toException())
        }

    }

    private fun addDataToList(snapshot: DataSnapshot) {
        val items = snapshot.children.iterator()

        if (items.hasNext()) {
            val toDoListIndex = items.next()
            val itemsIterator = toDoListIndex.children.iterator()

            while (itemsIterator.hasNext()) {

                val currentItem = itemsIterator.next()
                val todoItem = ToDoItem.create()

                val map = currentItem.value as HashMap<*, *>

                todoItem.objectId = currentItem.key
                todoItem.done = map["done"] as Boolean?
                todoItem.itemText = map["itemText"] as String?

                toDoItemList!!.add(todoItem)
            }
        }
    }

    override fun modifyItemsState(itemObjectId: String, isDone: Boolean, position: Int) {
        val itemReference = mDatabase.child(Constants.FIREBASE_ITEM).child(itemObjectId)
        itemReference.child("done").setValue(isDone)

    }

    override fun onItemDelete(itemObjectId: String, position: Int) {
        val itemReference = mDatabase.child(Constants.FIREBASE_ITEM).child(itemObjectId)
        itemReference.removeValue()
        recyclerView.invalidate()
        recreate()
    }

}