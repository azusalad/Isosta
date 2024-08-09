package io.github.azusalad.isosta.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

// The database will contain the followed users

@Entity(tableName = "user")
data class IsostaUser (
    @PrimaryKey
    val profileHandle: String,

    val profileLink: String,
    val profilePicture: String,
    val profileName: String,

    val profileDescription: String = "",
    val postCount: String = "",
    val followerCount: String = "",
    val followingCount: String = "",
    var pictureString: String? = null,  // Profile picture as Base64

    @Ignore
    val thumbnailList: List<Thumbnail>? = arrayListOf<Thumbnail>()
) {
    constructor(
        profileHandle: String,
        profileLink: String,
        profilePicture: String,
        profileName: String,
        profileDescription: String = "",
        postCount: String = "",
        followerCount: String = "",
        followingCount: String = "",
        pictureString: String? = null
    ): this(
        profileHandle = profileHandle,
        profileLink = profileLink,
        profilePicture = profilePicture,
        profileName = profileName,
        profileDescription = profileDescription,
        postCount = postCount,
        followerCount = followerCount,
        followingCount = followingCount,
        pictureString = pictureString,
        thumbnailList = null
    )
}
