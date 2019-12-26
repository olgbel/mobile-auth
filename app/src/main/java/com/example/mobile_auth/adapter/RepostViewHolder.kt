package com.example.mobile_auth.adapter

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_auth.R
import com.example.mobile_auth.dto.PostModel
import kotlinx.android.synthetic.main.item_repost.view.*
import kotlinx.android.synthetic.main.main_info.view.*
import kotlinx.android.synthetic.main.social_buttons_footer.view.*
import org.jetbrains.anko.toast

class RepostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {

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
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            main_info_post.authorTv.text = post.author
            main_info_post.contentTv.text = post.content

            main_info_repost.authorTv.text = post.source?.author
            main_info_repost.contentTv.text = post.source?.content

            social_btns_footer.likesTv.text = post.likes.count().toString()
            social_btns_footer.repostsTv.text = post.reposts.toString()
            social_btns_footer.commentsTv.text = post.comments.toString()

            if (post.likeActionPerforming) {
                likeBtn.setImageResource(R.drawable.ic_favorite_pending_24dp)
            } else if (post.likes.contains(post.author.toLong())) {
                likeBtn.setImageResource(R.drawable.ic_favorite_active_24dp)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            } else {
                likeBtn.setImageResource(R.drawable.ic_favorite_inactive_24dp)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
            }

            shareBtn.visibility = View.INVISIBLE
            repostsTv.visibility = View.INVISIBLE
        }

    }
}