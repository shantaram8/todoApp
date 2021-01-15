package com.example.todosecond

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ToDoItemRecyclerAdapter(
        toDoItemsList: MutableList<ToDoItem>,
        private val rowListener: ItemRowListener
) : RecyclerView.Adapter<ToDoItemViewHolder>() {

    private var itemList = toDoItemsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_items, parent, false)
        return ToDoItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        val item = itemList[position]
        val objectId: String = item.objectId.toString()
        val itemText: String = item.itemText.toString()
        val done: Boolean = item.done as Boolean

        holder.label.text = itemText
        holder.isDone.isChecked = done


        holder.isDone.setOnClickListener {
            rowListener.modifyItemsState(objectId, !done, position)
        }
        holder.ibDeleteObject.setOnClickListener {
            rowListener.onItemDelete(objectId, position)
        }
    }

    override fun getItemCount(): Int = itemList.size
}