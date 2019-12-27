package com.example.mobile_auth

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_auth.utils.API_SHARED_FILE
import com.example.mobile_auth.utils.AUTHENTICATED_SHARED_KEY
import com.example.mobile_auth.utils.isValid
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

@InternalCoroutinesApi
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isAuthenticated()) {
            val token = getSharedPreferences(
                API_SHARED_FILE,
                Context.MODE_PRIVATE
            )
                .getString(AUTHENTICATED_SHARED_KEY, "")
            Repository.createRetrofitWithAuth(token!!)

            val feedActivityIntent = Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(feedActivityIntent)
            finish()
        } else {
            btn_login.setOnClickListener {
                if (!edt_password.text.toString().isValid()) {
                    edt_password.error = R.string.invalid_password.toString()
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
                        dialog?.dismiss()
                        if (response.isSuccessful) {
                            toast(R.string.success)
                            setUserAuth(response.body()!!.token)
                            Repository.createRetrofitWithAuth(response.body()!!.token)
                            val feedActivityIntent =
                                Intent(this@MainActivity, FeedActivity::class.java)
                            startActivity(feedActivityIntent)
                            finish()
                        } else {
                            toast(R.string.authentication_failed)
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
