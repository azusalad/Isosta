package io.github.azusalad.isosta.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.azusalad.isosta.R
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.model.Thumbnail
import io.github.azusalad.isosta.ui.components.TextMessageScreen
import io.github.azusalad.isosta.ui.components.ThumbnailCard

@Composable
fun HomeScreen(
    isostaHomeUiState: IsostaHomeUiState,
    onThumbnailClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    // Switch statement shows different things depending on the IsostaUiState
    when (isostaHomeUiState) {
        is IsostaHomeUiState.Loading -> TextMessageScreen(text = "Loading thumbnails", modifier = modifier.fillMaxSize())
        is IsostaHomeUiState.Success -> ThumbnailList(
            thumbnailList = isostaHomeUiState.thumbnailPhotos, onThumbnailClicked = onThumbnailClicked, modifier = modifier.fillMaxWidth(), contentPadding = contentPadding,
        )
        //is IsostaUiState.Success -> TextMessageScreen(text = isostaUiState.thumbnailPhotos, modifier = modifier.fillMaxWidth())
        is IsostaHomeUiState.Error -> TextMessageScreen(
            text = "There was a error loading thumbnails:\n\n" + isostaHomeUiState.errorString,
            modifier = modifier.fillMaxSize())
    }
}

@Composable
fun ThumbnailList(
    thumbnailList: List<Thumbnail>,
    onThumbnailClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(contentPadding = contentPadding, modifier = modifier) {
        items(thumbnailList) { thumbnail ->
            ThumbnailCard(
                thumbnail = thumbnail,
                onThumbnailClicked = onThumbnailClicked,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun FollowList(
    userList: List<IsostaUser>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(contentPadding = contentPadding, modifier = modifier) {
        items(userList) { isostaUser ->
            UserCard(
                user = isostaUser
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun UserCard(
    user: IsostaUser,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(modifier = modifier.padding(5.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(user.profilePicture)
                    .crossfade(true)
                    .setHeader("User-Agent", "Mozilla/5.0")
                    .build(),
                error = painterResource(R.drawable.broken_image),
                placeholder = painterResource(R.drawable.hourglass_top),
                contentDescription = "Comment profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f / 1f)
                    .clip(CircleShape)
                    .weight(1f)
            )
            Column(modifier = Modifier.padding(10.dp).weight(5f)) {
                Text(
                    text = user.profileName,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = user.profileHandle,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val yui = "https://avatars.githubusercontent.com/u/68360714?v=4"
    val thumbnailList = arrayListOf<Thumbnail>()

    thumbnailList.add(
        Thumbnail(
            picture = yui,
            postLink = "",
            text = "description 1"
        )
    )
    thumbnailList.add(
        Thumbnail(
            picture = yui,
            postLink = "",
            text = "description description description description description description description description description description description description description description "
        )
    )
    ThumbnailList(
        thumbnailList = thumbnailList,
        onThumbnailClicked = {}
    )
}

@Preview
@Composable
fun FollowListPreview() {
    val yui = "https://avatars.githubusercontent.com/u/68360714?v=4"
    val userList = arrayListOf<IsostaUser>()
    userList.add(
        IsostaUser(
            profilePicture = yui,
            profileName = "Yui",
            profileHandle = "@yui",
            profileLink = "",
        )
    )
    userList.add(
        IsostaUser(
            profilePicture = yui,
            profileName = "Yui2",
            profileHandle = "@yui2",
            profileLink = "",
        )
    )
    FollowList(
        userList = userList
    )
}
