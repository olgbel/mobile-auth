package com.example.mobile_auth

import android.graphics.Bitmap
import com.example.mobile_auth.api.API
import com.example.mobile_auth.api.AuthRequestParams
import com.example.mobile_auth.api.CreatePostRequest
import com.example.mobile_auth.api.RegistrationRequestParams
import com.example.mobile_auth.api.interceptor.InjectAuthTokenInterceptor
import com.example.mobile_auth.dto.AttachmentModel
import com.example.mobile_auth.dto.PostRequestDto
import com.example.mobile_auth.dto.PostType
import com.example.mobile_auth.utils.BASE_URL
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

object Repository {

    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun createRetrofitWithAuth(authToken: String) {
        val httpLoggerInterceptor = HttpLoggingInterceptor()
        // Указываем, что хотим логировать тело запроса.
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor(authToken))
            .addInterceptor(httpLoggerInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //создаем API на основе нового retrofit-клиента
        API = retrofit.create(com.example.mobile_auth.api.API::class.java)
    }


    // Ленивое создание API
    private var API: API =
        retrofit.create(com.example.mobile_auth.api.API::class.java)


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

    suspend fun getById(id: Long) = API.getPostById(id)

    suspend fun createPost(content: String, attachmentModel: AttachmentModel?) = API.createPost(
        CreatePostRequest(
//            postType = PostType.POST,
            content = content,
            attachment = attachmentModel
        )
    )

    suspend fun getPosts() = API.getPosts()

    suspend fun getPostsAfter(id: Long) = API.getPostsAfter(id)

    suspend fun getPostsBefore(id: Long) = API.getPostsBefore(id)

    suspend fun getRecentPosts() = API.getRecentPosts()

    suspend fun likedByMe(id: Long) = API.likedByMe(id)

    suspend fun cancelMyLike(id: Long) = API.cancelMyLike(id)

    suspend fun createRepost(postId: Long, content: String?) = API.createRepost(
        postId,
        PostRequestDto(content = content)
    )

    suspend fun upload(bitmap: Bitmap): Response<AttachmentModel> {
        // Создаем поток байтов
        val bos = ByteArrayOutputStream()
        // Помещаем Bitmap в качестве JPEG в этот поток
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val reqFIle =
            // Создаем тип медиа и передаем массив байтов с потока
            RequestBody.create(MediaType.parse("image/jpeg"), bos.toByteArray())
        val body =
        // Создаем multipart объект, где указываем поле, в котором
            // содержатся посылаемые данные, имя файла и медиафайл
            MultipartBody.Part.createFormData("file", "image.jpg", reqFIle)
        return API.uploadImage(body)
    }
}