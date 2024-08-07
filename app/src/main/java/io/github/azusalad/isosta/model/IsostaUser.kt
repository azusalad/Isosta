package io.github.azusalad.isosta.model

data class IsostaUser (
    val profileLink: String,
    val profilePicture: String,
    val profileName: String,
    val profileHandle: String,

    val profileDescription: String = "",
    val postCount: String = "",
    val followerCount: String = "",
    val followingCount: String = "",
    val thumbnailList: List<Thumbnail> = arrayListOf()
)
