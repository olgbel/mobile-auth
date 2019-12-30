package com.example.mobile_auth.api

import com.example.mobile_auth.dto.AttachmentModel
import com.example.mobile_auth.dto.PostModel
import com.example.mobile_auth.dto.PostRequestDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

data class AuthRequestParams(val username: String, val password: String)

data class Token(val token: String)

data class RegistrationRequestParams(val username: String, val password: String)

data class CreatePostRequest(val id: Long = 0,
                             val content: String,
                             val attachment: AttachmentModel? = null)

interface API {
    @POST("api/v1/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("api/v1/registration")
    suspend fun register(@Body registrationRequestParams: RegistrationRequestParams): Response<Token>

    @POST("/api/v1/posts")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest): Response<Void>

    @GET("api/v1/posts")
    suspend fun getPosts(): Response<List<PostModel>>

    @GET("api/v1/posts/recent")
    suspend fun getRecentPosts(): Response<List<PostModel>>

    @GET("api/v1/posts/after/{id}")
    suspend fun getPostsAfter(@Path("id") id: Long): Response<List<PostModel>>

    @GET("api/v1/posts/before/{id}")
    suspend fun getPostsBefore(@Path("id") id: Long): Response<List<PostModel>>

    @GET("api/v1/posts/{id}")
    suspend fun getPostById(@Path("id") id: Long): Response<PostModel>

    @POST("api/v1/posts/like/{id}")
    suspend fun likedByMe(@Path("id") id: Long): Response<PostModel>

    @POST("api/v1/posts/dislike/{id}")
    suspend fun cancelMyLike(@Path("id") id: Long): Response<PostModel>

    @POST("api/v1/posts/repost/{id}")
    suspend fun createRepost(@Path("id") postId: Long, @Body createPostRequest: PostRequestDto): Response<List<PostModel>>

    @Multipart
    @POST("api/v1/media")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<AttachmentModel>
}