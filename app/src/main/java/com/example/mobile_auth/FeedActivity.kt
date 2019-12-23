package com.example.mobile_auth

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_auth.adapter.PostAdapter
import com.example.mobile_auth.dto.PostModel
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

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
            val result = Repository.getPosts()

            println("feed activity onStart: $result")
            dialog?.dismiss()
            if (result.isSuccessful) {
                with(container) {
                    layoutManager = LinearLayoutManager(this@FeedActivity)
                    adapter = PostAdapter(result.body() ?: emptyList()).apply {
                        likeBtnClickListener = this@FeedActivity
                        repostBtnClickListener = this@FeedActivity
                    }
                }
            } else {
                Toast.makeText(this@FeedActivity, R.string.error_occured, Toast.LENGTH_SHORT)
            }
        }
    }

    override fun onLikeBtnClicked(item: PostModel, position: Int) {
        launch {
            item.likeActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)
                val response = if (item.likedByMe) {
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

    override fun onShareBtnClicked(item: PostModel, position: Int) {
//        launch {


            println("on share button clicked")
            item.repostActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)
                val postId = adapter?.getItemId(position)

                val dialog = AlertDialog.Builder(context)
                    .setView(R.layout.activity_create_post)
                    .setCancelable(true)
                    .show()
                dialog.createPostBtn.setOnClickListener {
                    val txt = dialog.contentEdt.text.toString()
                    launch {
                        val response = Repository.createRepost(postId!!)
                        println("response: $response")
                    }
                    println("entered text: $txt")
                    dialog.dismiss()
                }

            }
//        }
    }
}