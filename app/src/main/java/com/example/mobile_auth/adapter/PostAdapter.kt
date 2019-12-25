package com.example.mobile_auth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_auth.R
import com.example.mobile_auth.dto.PostModel
import com.example.mobile_auth.dto.PostType

class PostAdapter(var list: MutableList<PostModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var repostBtnClickListener: OnShareBtnClickListener? = null

    private val ITEM_TYPE_POST = 1
    private val ITEM_TYPE_REPOST = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_POST) {
            val postView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
            PostViewHolder(this, postView)
        } else {
            val repostView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_repost, parent, false)
            RepostViewHolder(this, repostView)
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PostViewHolder -> holder.bind(list[position])
            is RepostViewHolder -> holder.bind(list[position])
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (list[position].postType == PostType.POST){
            ITEM_TYPE_POST
        } else ITEM_TYPE_REPOST
    }

    interface OnLikeBtnClickListener{
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnShareBtnClickListener{
        fun onShareBtnClicked(
            item: PostModel,
            position: Int,
            postsList: MutableList<PostModel>
        )
    }
}