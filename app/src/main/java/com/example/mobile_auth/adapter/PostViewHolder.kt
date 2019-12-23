package com.example.mobile_auth.adapter

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_auth.R
import com.example.mobile_auth.dto.PostModel
import kotlinx.android.synthetic.main.main_info.view.*
import kotlinx.android.synthetic.main.social_buttons_footer.view.*

class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.likeActionPerforming) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.like_in_progress),
                            Toast.LENGTH_SHORT
                        )
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }
            shareBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    if (item.repostActionPerforming){
                        Toast.makeText(context, context.getString(R.string.repost_in_progress), Toast.LENGTH_SHORT)
                    }
                    else {
                        adapter.repostBtnClickListener?.onShareBtnClicked(item, currentPosition)
                    }
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.ownerName
            contentTv.text = post.content
            likesTv.text = post.likes.count().toString()
            repostsTv.text = post.reposts.toString()
            commentsTv.text = post.comments.toString()

            if (post.likeActionPerforming) {
                likeBtn.setImageResource(R.drawable.ic_favorite_pending_24dp)
            } else if (post.likedByMe) {
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