package com.example.todosecond

interface ItemRowListener {

    fun modifyItemsState(itemObjectId: String, isDone: Boolean)
    fun onItemDelete(itemObjectId: String)
}