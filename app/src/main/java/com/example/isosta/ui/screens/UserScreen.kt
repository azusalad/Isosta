package com.example.isosta.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

@Composable
fun userBio(
    user: IsostaUser,
    postCount: Int,
    followerCount: Int,
    followingCount: Int
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
fun userBioPreview() {
    userBio(
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
