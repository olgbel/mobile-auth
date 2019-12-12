package com.example.mobile_auth

import com.example.mobile_auth.api.API
import com.example.mobile_auth.api.AuthRequestParams
import com.example.mobile_auth.api.RegistrationRequestParams
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Repository {

    // Ленивое создание Retrofit экземпляра
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ncraftmedia.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // Ленивое создание API
    private val API: API by lazy {
        retrofit.create(com.example.mobile_auth.api.API::class.java)
    }

    suspend fun authenticate(login: String, password: String) = API.authenticate(
        AuthRequestParams(login, password)
    )

    suspend fun register(login: String, password: String) =
        API.register(
            RegistrationRequestParams(
                login,
                password
            )
        )
}