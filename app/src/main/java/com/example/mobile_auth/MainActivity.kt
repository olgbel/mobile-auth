package com.example.mobile_auth

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity

@InternalCoroutinesApi
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isAuthenticated()) {
            val feedActivityIntent = Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(feedActivityIntent)
            finish()
        } else {
            btn_login.setOnClickListener {
                if (!isValid(edt_password.text.toString())) {
                    edt_password.error = "Password is incorrect"
                } else {
                    launch {
                        dialog =
                            indeterminateProgressDialog(
                                message = R.string.please_wait,
                                title = R.string.authentication
                            ) {
                                setCancelable(false)
                            }
                        val response =
                            Repository.authenticate(
                                edt_login.text.toString(),
                                edt_password.text.toString()
                            )
                        println("response $response")
                        println("login: $edt_login.text.toString()")
                        println("password: $edt_password.text.toString()")
                        dialog?.dismiss()
                        if (response.isSuccessful) {
                            println("Success")
                            Toast.makeText(this@MainActivity, R.string.success, Toast.LENGTH_SHORT)
                                .show()
                            setUserAuth(response.body()!!.token)
                            val feedActivityIntent =
                                Intent(this@MainActivity, FeedActivity::class.java)
                            startActivity(feedActivityIntent)
                            finish()
                        } else {
                            println("failed")
                            Toast.makeText(
                                this@MainActivity,
                                R.string.authentication_failed,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        btn_registration.setOnClickListener {
            val registrationIntent = Intent(this@MainActivity, RegistrationActivity::class.java)
            startActivity(registrationIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (isAuthenticated()) {
            startActivity<FeedActivity>()
            finish()
        }
    }

    private fun isAuthenticated() =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY, ""
        )?.isNotEmpty() ?: false

    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(AUTHENTICATED_SHARED_KEY, token)
            .commit()

    override fun onStop() {
        super.onStop()
        cancel()
        dialog?.dismiss()
    }
}
