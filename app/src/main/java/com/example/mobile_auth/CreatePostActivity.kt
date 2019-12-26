package com.example.mobile_auth

import android.app.ProgressDialog
import android.app.ProgressDialog.show
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import java.io.IOException

class CreatePostActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        createPostBtn.setOnClickListener {
            launch {
                dialog = ProgressDialog(this@CreatePostActivity).apply {
                    setMessage(this@CreatePostActivity.getString(R.string.please_wait))
                    setTitle(R.string.create_new_post)
                    setCancelable(false)
                    setProgressBarIndeterminate(true)
                    show()
                }
                try {
                    val result = Repository.createPost(contentEdt.text.toString())
                    if (result.isSuccessful) {
                        handleSuccessfullResult()
                    } else {
                        handleFailedResult()
                    }
                } catch (e: IOException) {
                    handleFailedResult()
                } finally {
                    dialog?.dismiss()
                }

            }
        }
    }

    private fun handleSuccessfullResult() {
        toast(R.string.post_created_successfully)
        finish()
    }

    private fun handleFailedResult() {
        toast(R.string.error_occured)
    }
}