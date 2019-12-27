package com.example.mobile_auth.dto

import com.example.mobile_auth.utils.BASE_URL

enum class AttachmentType {
    IMAGE, AUDIO, VIDEO
}

data class AttachmentModel(val id: String, val type: AttachmentType){
    val url
        get() = "$BASE_URL/api/v1/static/$id"
}

enum class PostType {
    POST, REPOST
}

data class PostModel(
    val id: Long,
    val source: PostModel?,
    val author: String,
    val created: Int,
    var content: String? = null,
    var likes: Set<Long> = setOf(),
    var reposts: Int = 0,
    var repostedByMe: Boolean = false,
    var comments: Int = 0,
    val youtubeURL: String? = null,
    val postType: PostType = PostType.POST,
    val address: String? = null,
    val coordinates: Pair<Double, Double>? = null,
    val attachment: AttachmentModel?
) {
    var likeActionPerforming = false
    var repostActionPerforming = false

    fun updateLikes(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        likes = updatedModel.likes
    }
    fun updatePost(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        likes = updatedModel.likes
        content = updatedModel.content
        reposts = updatedModel.reposts
        repostedByMe = updatedModel.repostedByMe
    }
}