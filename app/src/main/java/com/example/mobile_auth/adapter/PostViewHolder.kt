package com.example.mobile_auth.adapter

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_auth.R
import com.example.mobile_auth.dto.PostModel
import kotlinx.android.synthetic.main.main_info.view.*
import kotlinx.android.synthetic.main.social_buttons_footer.view.*
import org.jetbrains.anko.toast

class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition - 1]
                    if (item.likeActionPerforming) {
                        context.toast(R.string.like_in_progress)
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }
            shareBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition - 1]
                    if (item.repostActionPerforming){
                        Toast.makeText(context, context.getString(R.string.repost_in_progress), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        adapter.repostBtnClickListener?.onShareBtnClicked(item, currentPosition, adapter.list)
                    }
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.author
            contentTv.text = post.content
            likesTv.text = post.likes.count().toString()
            repostsTv.text = post.reposts.toString()
            commentsTv.text = post.comments.toString()

            if (post.likeActionPerforming) {
                likeBtn.setImageResource(R.drawable.ic_favorite_pending_24dp)
            } else if (post.likes.contains(post.author.toLong())) {
                likeBtn.setImageResource(R.drawable.ic_favorite_active_24dp)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            } else {
                likeBtn.setImageResource(R.drawable.ic_favorite_inactive_24dp)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
            }

            if (post.repostActionPerforming) {
                shareBtn.setImageResource(R.drawable.ic_reposts_pending)
            } else if (post.repostedByMe) {
                shareBtn.setImageResource(R.drawable.ic_reposts_active)
                repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            } else {
                shareBtn.setImageResource(R.drawable.ic_reposts_inactive)
                repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
            }
        }
    }
}