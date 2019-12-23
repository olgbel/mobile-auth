package com.example.mobile_auth.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_auth.R
import com.example.mobile_auth.dto.PostModel
import kotlinx.android.synthetic.main.item_repost.view.*
import kotlinx.android.synthetic.main.main_info.view.*
import kotlinx.android.synthetic.main.social_buttons_footer.view.*

class RepostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {

    fun bind(post: PostModel) {
        with(itemView) {
            main_info_post.authorTv.text = post.ownerName
            main_info_post.contentTv.text = post.content

            main_info_repost.authorTv.text = post.source?.ownerName
            main_info_repost.contentTv.text = post.source?.content

            social_btns_footer.likesTv.text = "0"
            social_btns_footer.repostsTv.text = "0"
            social_btns_footer.commentsTv.text = "0"

//            authorTv.text = post.ownerName
//            contentTv.text = post.content
//            likesTv.text = post.likes.count().toString()
//            repostsTv.text = post.reposts.toString()
//            commentsTv.text = post.comments.toString()
//
//            if (post.likeActionPerforming) {
//                likeBtn.setImageResource(R.drawable.ic_favorite_pending_24dp)
//            } else if (post.likedByMe) {
//                likeBtn.setImageResource(R.drawable.ic_favorite_active_24dp)
//                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
//            } else {
//                likeBtn.setImageResource(R.drawable.ic_favorite_inactive_24dp)
//                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
//            }
//
//            if (post.repostActionPerforming) {
//                shareBtn.setImageResource(R.drawable.ic_reposts_pending)
//            } else if (post.repostedByMe) {
//                shareBtn.setImageResource(R.drawable.ic_reposts_active)
//                repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
//            } else {
//                shareBtn.setImageResource(R.drawable.ic_reposts_inactive)
//                repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
//            }
        }

    }
}