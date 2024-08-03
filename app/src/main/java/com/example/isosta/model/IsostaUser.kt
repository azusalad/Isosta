package com.example.isosta.model

data class IsostaUser (
    val profilePicture: String,
    val profileName: String,
    val profileHandle: String,
    val profileLink: String,

    val profileDescription: String = "",
    val postCount: String = "",
    val followerCount: String = "",
    val followingCount: String = "",
    val thumbnailList: List<Thumbnail> = arrayListOf()
)
