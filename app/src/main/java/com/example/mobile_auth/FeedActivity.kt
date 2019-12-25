package com.example.mobile_auth

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_auth.adapter.PostAdapter
import com.example.mobile_auth.dto.PostModel
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class FeedActivity : AppCompatActivity(), CoroutineScope by MainScope(),
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnShareBtnClickListener {

    private var dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        fab.setOnClickListener {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        launch {
            dialog = ProgressDialog(this@FeedActivity).apply {
                setMessage(this@FeedActivity.getString(R.string.please_wait))
                setTitle(R.string.downloading_posts)
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            val result = Repository.getRecentPosts()

            dialog?.dismiss()
            if (result.isSuccessful) {
                with(container) {
                    layoutManager = LinearLayoutManager(this@FeedActivity)
                    adapter = PostAdapter(result.body() as MutableList<PostModel>).apply {
                        likeBtnClickListener = this@FeedActivity
                        repostBtnClickListener = this@FeedActivity
                    }
                }
            } else {
                toast(R.string.error_occured)
            }
        }
    }

    override fun onLikeBtnClicked(item: PostModel, position: Int) {
        launch {
            item.likeActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)
                val response = if (item.likes.contains(item.author.toLong())) {
                    Repository.cancelMyLike(item.id)
                } else {
                    Repository.likedByMe(item.id)
                }
                item.likeActionPerforming = false
                if (response.isSuccessful) {
                    item.updateLikes(response.body()!!)
                }
                adapter?.notifyItemChanged(position)
            }
        }
    }

    override fun onShareBtnClicked(
        item: PostModel,
        position: Int,
        postsList: MutableList<PostModel>
    ) {
            item.repostActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)

                val dialog = AlertDialog.Builder(context)
                    .setView(R.layout.activity_create_post)
                    .setCancelable(true)
                    .show()
                dialog.createPostBtn.setOnClickListener {
                    val txt = dialog.contentEdt.text.toString()
                    launch {
                        val response = Repository.createRepost(item.id, txt)
                        if (response.isSuccessful){
                            item.updatePost(response.body()?.get(0)!!)
                            adapter?.notifyItemChanged(position)
                            postsList.add(response.body()?.get(1)!!)
                            adapter?.notifyDataSetChanged()
                        }
                    }
                    dialog.dismiss()
                    item.repostActionPerforming = false
                }
            }
    }
}