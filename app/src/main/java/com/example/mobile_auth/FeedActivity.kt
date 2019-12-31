package com.example.mobile_auth

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mobile_auth.NotificationHelper.comeBackNotification
import com.example.mobile_auth.NotificationHelper.isFirstTime
import com.example.mobile_auth.NotificationHelper.setLastVisitTime
import com.example.mobile_auth.NotificationHelper.setNotFirstTime
import com.example.mobile_auth.adapter.PostAdapter
import com.example.mobile_auth.dto.PostModel
import com.example.mobile_auth.utils.SHOW_NOTIFICATION_AFTER_UNVISITED_MS
import com.example.mobile_auth.utils.UserNotHereWorker
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

class FeedActivity : AppCompatActivity(), CoroutineScope by MainScope(),
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnShareBtnClickListener {

    private var dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleJob()
        setContentView(R.layout.activity_feed)

        fab.setOnClickListener {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }

        requestToken()
    }

    private fun requestToken() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@FeedActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@FeedActivity, code, 9000).show()
                return
            }
            longToast(getString(R.string.google_play_unavailable))
            return
        }

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            launch {
                println(it.token)
                val response = Repository.registerPushToken(it.token)
            }
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
                    adapter = PostAdapter(
                        this@FeedActivity,
                        requireNotNull(result.body()).toMutableList()
                    ).apply {
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
                    item.updateLikes(requireNotNull(response.body()))
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
                .show()
            dialog.createPostBtn.setOnClickListener {
                val txt = dialog.contentEdt.text.toString()
                launch {
                    val response = Repository.createRepost(item.id, txt)
                    if (response.isSuccessful) {
                        item.updatePost(requireNotNull(response.body()?.get(0)))
                        adapter?.notifyItemChanged(position)
                        postsList.add(0, requireNotNull(response.body()?.get(1)))
                        adapter?.notifyDataSetChanged()
                    }
                }
                dialog.dismiss()
                item.repostActionPerforming = false
            }
        }
    }

    private fun scheduleJob() {
        val checkWork =
            PeriodicWorkRequestBuilder<UserNotHereWorker>(
                SHOW_NOTIFICATION_AFTER_UNVISITED_MS,
                TimeUnit.MILLISECONDS
            )
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "user_present_work",
                ExistingPeriodicWorkPolicy.KEEP, checkWork
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFirstTime(this)) {
            comeBackNotification(this)
            setNotFirstTime(this)
            setLastVisitTime(this, System.currentTimeMillis())
        } else {
            setLastVisitTime(this, System.currentTimeMillis())
        }

        cancel()
    }
}