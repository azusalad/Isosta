package io.github.azusalad.isosta.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "isostaUser")
data class IsostaUser (
    @PrimaryKey(autoGenerate = false)
    val profileLink: String,
    val profilePicture: String,
    val profileName: String,
    val profileHandle: String,

    val date: Int = 0,
    val profileDescription: String = "",
    val postCount: String = "",
    val followerCount: String = "",
    val followingCount: String = "",
    val thumbnailList: List<Thumbnail> = arrayListOf()
)
