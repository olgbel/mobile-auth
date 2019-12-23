package com.example.mobile_auth

import android.app.ProgressDialog
import android.app.ProgressDialog.show
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.IOException

class CreatePostActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        createPostBtn.setOnClickListener {
            println("cretePost listener")
            launch {
                // Показываем крутилку
                dialog = ProgressDialog(this@CreatePostActivity).apply {
                    setMessage(this@CreatePostActivity.getString(R.string.please_wait))
                    setTitle(R.string.create_new_post)
                    setCancelable(false)
                    setProgressBarIndeterminate(true)
                    show()
                }
                // Обворачиваем в try catch, потому что возможны ошибки при соединении с сетью
                try {
                    val result = Repository.createPost(contentEdt.text.toString())
                    println("result: $result")
                    if (result.isSuccessful) {
                        // обрабатываем успешное создание поста
                        handleSuccessfullResult()
                    } else {
                        // обрабоатываем ошибку
                        handleFailedResult()
                    }
                } catch (e: IOException) {
                    // обрабатываем ошибку
                    handleFailedResult()
                } finally {
                    // закрываем диалог
                    dialog?.dismiss()
                }

            }
        }

    }

    private fun handleSuccessfullResult() {
        Toast.makeText(this, R.string.post_created_successfully, Toast.LENGTH_SHORT)
        finish()
    }

    private fun handleFailedResult() {
        Toast.makeText(this, R.string.error_occured, Toast.LENGTH_SHORT)
    }
}