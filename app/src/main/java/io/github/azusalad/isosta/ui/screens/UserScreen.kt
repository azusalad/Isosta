package io.github.azusalad.isosta.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.azusalad.isosta.R
import io.github.azusalad.isosta.model.IsostaUser
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.azusalad.isosta.model.Thumbnail
import io.github.azusalad.isosta.ui.components.TextMessageScreen
import io.github.azusalad.isosta.ui.components.ThumbnailCard
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
    isostaUserUiState: IsostaUserUiState,
    onThumbnailClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Switch statement shows different things depending on the IsostaUserUiState
    when (isostaUserUiState) {
        is IsostaUserUiState.Loading -> {
            TextMessageScreen(text = "Loading user", modifier = modifier.fillMaxSize())
        }
        is IsostaUserUiState.Success -> UserColumn(
            user = isostaUserUiState.user,
            onThumbnailClicked = onThumbnailClicked
        )
        is IsostaUserUiState.Error -> TextMessageScreen(
            text = "There was a error loading the user:\n\n" + isostaUserUiState.errorString,
            modifier = modifier.fillMaxSize())
    }
}

@Composable
fun UserColumn(
    user: IsostaUser,
    onThumbnailClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        // User header
        item {
            UserBio(
                user = user,
                postCount = user.postCount,
                followerCount = user.followerCount,
                followingCount = user.followingCount
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }
        // Thumbnail lazy column
        items(user.thumbnailList) { thumbnail ->
            ThumbnailCard(
                thumbnail = thumbnail,
                onThumbnailClicked = {onThumbnailClicked(thumbnail.postLink)},
                modifier = Modifier.padding(8.dp)
            )

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserBio(
    user: IsostaUser,
    postCount: String,
    followerCount: String,
    followingCount: String,
    viewModel: ThumbnailViewModel = viewModel(factory = ThumbnailViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
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
                .clip(CircleShape).size(200.dp),
        )
        // User name
        Text(
            text = user.profileName,
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.combinedClickable(
                onClick = {},
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    clipboardManager.setText(AnnotatedString(user.profileName))
                }
            )
        )
        // User handle
        Text(
            text = user.profileHandle,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.combinedClickable(
                onClick = {},
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    clipboardManager.setText(AnnotatedString(user.profileHandle))
                }
            )
        )
        Spacer(
            modifier = modifier.height(10.dp)
        )
        // Bio stats
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            StatText(topText = postCount, bottomText = "posts")
            StatText(topText = followerCount, bottomText = "followers")
            StatText(topText = followingCount, bottomText = "following")
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(150.dp)
                .height(50.dp)
                .background(MaterialTheme.colorScheme.primary)  // TODO: use secondary for depressed state
                .clickable(
                    onClick = {
                        /* TODO: Add button depressed state */
                        coroutineScope.launch {
                            for (thumbnail in user.thumbnailList) {
                                viewModel.saveThumbnail(thumbnail)
                            }
                        }
                    }
                )

        ) {
            Text(
                text = "Follow ♡",  //  TODO: Add following ♥ when button depressed
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}

@Composable
fun StatText(
    topText: String,
    bottomText: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = topText,
            style = MaterialTheme.typography.displaySmall,
        )
        Text(
            text = bottomText,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}


@Preview
@Composable
fun UserColumnPreview() {
    val yui = "https://avatars.githubusercontent.com/u/68360714?v=4"
    val thumbnailList = arrayListOf<Thumbnail>()

    thumbnailList.add(
        Thumbnail(
            picture = yui,
            postLink = "",
            text = "description 1",
            sourceHandle = ""
        )
    )
    thumbnailList.add(
        Thumbnail(
            picture = yui,
            postLink = "",
            sourceHandle = "",
            text = "description description description description description description description description description description description description description description "
        )
    )

    UserColumn(
        user = IsostaUser(
            profileHandle = "@kita",
            profileLink = "",
            profileName = "Kita Ikuyo",
            profilePicture = yui,

            followerCount = "123",
            followingCount = "456k",
            postCount = "789",
            thumbnailList = thumbnailList
        ),
        onThumbnailClicked = {}
    )
}
