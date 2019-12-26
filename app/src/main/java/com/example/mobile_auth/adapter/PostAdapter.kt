package com.example.mobile_auth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_auth.*
import com.example.mobile_auth.dto.PostModel
import com.example.mobile_auth.dto.PostType

class PostAdapter(var list: MutableList<PostModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var repostBtnClickListener: OnShareBtnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_POST -> {
                val postView =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
                PostViewHolder(this, postView)
            }
            ITEM_TYPE_REPOST -> {
                val repostView =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_repost, parent, false)
                RepostViewHolder(this, repostView)
            }
            ITEM_HEADER -> {
                HeaderViewHolder(
                    this,
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_load_new,
                        parent,
                        false
                    )
                )
            }
            else -> {
                FooterViewHolder(
                    this,
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_load_more,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount() = list.size + 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> holder.bind(list[position - 1])
            is RepostViewHolder -> holder.bind(list[position - 1])
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when {
            position == 0 -> ITEM_HEADER
            position == list.size + 1 -> ITEM_FOOTER
            list[position - 1].postType == PostType.POST -> ITEM_TYPE_POST
            else -> ITEM_TYPE_REPOST
        }
    }

    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnShareBtnClickListener {
        fun onShareBtnClicked(
            item: PostModel,
            position: Int,
            postsList: MutableList<PostModel>
        )
    }
}