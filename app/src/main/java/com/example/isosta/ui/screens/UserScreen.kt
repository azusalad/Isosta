package com.example.isosta.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.isosta.R
import com.example.isosta.model.IsostaUser
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.isosta.model.IsostaComment
import com.example.isosta.model.Thumbnail
import com.example.isosta.ui.components.ThumbnailCard

@Composable
fun UserColumn(
    user: IsostaUser,
    postCount: Int,
    followerCount: Int,
    followingCount: Int,
    thumbnailList: List<Thumbnail>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            UserBio(
                user = user,
                postCount = postCount,
                followerCount = followerCount,
                followingCount = followingCount
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }
        items(thumbnailList) { thumbnail ->
            ThumbnailCard(
                thumbnail = thumbnail,
                onThumbnailClicked = {/* TODO: Implement this function */},
                modifier = Modifier.padding(8.dp)
            )

        }
    }
}

@Composable
fun UserBio(
    user: IsostaUser,
    postCount: Int,
    followerCount: Int,
    followingCount: Int,
    modifier: Modifier = Modifier
) {
    Column {
        // Profile picture
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(user.profilePicture)
                .crossfade(true)
                .setHeader("User-Agent", "Mozilla/5.0")
                .build(),
            error = painterResource(R.drawable.broken_image),
            placeholder = painterResource(R.drawable.hourglass_top),
            contentDescription = "Profile picture",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .clip(CircleShape),
        )
        // User name
        Text(
            text = "Kita Ikuyo"
        )
        // User handle
        Text(
            text = "@kita"
        )
        Row {
            Text("posts")
            Text("followers")
            Text("following")
        }
    }
}

@Preview
@Composable
fun UserBioPreview() {
    UserBio(
        user = IsostaUser(
            profileHandle = "@kita",
            profileLink = "",
            profileName = "Kita Ikuyo",
            profilePicture = "https://avatars.githubusercontent.com/u/68360714?v=4",
            ),
        followerCount = 123,
        followingCount = 456,
        postCount = 789
    )
}

@Preview
@Composable
fun UserColumnPreview() {
    val yui = "https://avatars.githubusercontent.com/u/68360714?v=4"
    val thumbnailList = arrayListOf<Thumbnail>()

    thumbnailList.add(Thumbnail(picture = yui, postLink = "", text = "description 1"))
    thumbnailList.add(Thumbnail(picture = yui, postLink = "", text = "description description description description description description description description description description description description description description "))

    UserColumn(
        user = IsostaUser(
            profileHandle = "@kita",
            profileLink = "",
            profileName = "Kita Ikuyo",
            profilePicture = yui,
        ),
        followerCount = 123,
        followingCount = 456,
        postCount = 789,
        thumbnailList = thumbnailList
    )
}
