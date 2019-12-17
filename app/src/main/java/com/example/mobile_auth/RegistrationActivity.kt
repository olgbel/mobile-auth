package com.example.mobile_auth

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.launch
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.toast

@InternalCoroutinesApi
class RegistrationActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btn_register.setOnClickListener {
            val password = edt_registration_password.text.toString()
            val repeatedPassword = edt_registration_repeat_password.text.toString()
            if (password != repeatedPassword) {
                toast(R.string.different_passwords)
            } else if (!isValid(password)) {
                toast(R.string.incorrect_password_mask)
            } else {
                launch {
                    dialog =
                        indeterminateProgressDialog(
                            message = R.string.please_wait,
                            title = R.string.signup
                        ) {
                            setCancelable(false)
                        }
                    val response =
                        Repository.register(
                            edt_registration_login.toString(),
                            password
                        )
                    dialog?.dismiss()
                    if (response.isSuccessful) {
                        toast(R.string.success)
                        setUserAuth(response.body()!!.token)
                        finish()
                    } else {
                        toast(R.string.registration_failed)
                    }
                }
            }
        }
    }

    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(AUTHENTICATED_SHARED_KEY, token)
            .commit()

    override fun onStop() {
        super.onStop()
        dialog?.dismiss()
        cancel()
    }
}