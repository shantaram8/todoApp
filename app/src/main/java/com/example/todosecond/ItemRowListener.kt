package com.example.todosecond

interface ItemRowListener {

    fun modifyItemsState(itemObjectId: String, isDone: Boolean, position: Int)
    fun onItemDelete(itemObjectId: String, position: Int)
}