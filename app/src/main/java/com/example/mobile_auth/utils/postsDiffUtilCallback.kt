package com.example.mobile_auth.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.mobile_auth.dto.PostModel

class postsDiffUtilCallback(oldList: List<PostModel>, newList: List<PostModel>) : DiffUtil.Callback() {

    val oldList: List<PostModel> = emptyList()
    val newList: List<PostModel> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPost = oldList[oldItemPosition]
        val newPost = newList[newItemPosition]
        return oldPost.id == newPost.id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPost = oldList[oldItemPosition]
        val newPost = newList[newItemPosition]
        return oldPost.reposts == newPost.reposts &&
                oldPost.likes == newPost.likes &&
                oldPost.content.equals(newPost.content) &&
                oldPost.source?.equals(newPost.source) ?: 1 == 1
    }

}