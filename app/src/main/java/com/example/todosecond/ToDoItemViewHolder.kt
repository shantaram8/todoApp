package com.example.todosecond

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    val label: TextView = itemView.findViewById(R.id.tv_item_text)
    val isDone: CheckBox = itemView.findViewById(R.id.cb_item_is_done)
    val ibDeleteObject: ImageButton = itemView.findViewById(R.id.iv_cross)

}