package io.github.azusalad.isosta.ui.components

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.azusalad.isosta.R
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.model.Thumbnail
import io.github.azusalad.isosta.ui.screens.stringToBitmap

@Composable
fun TextMessageScreen(
    text: String,
    modifier: Modifier = Modifier
) {
    println("LOG: Loading text message screen with the text: " + text)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            //fontSize = 30.sp,
            //lineHeight = 75.sp,
            //color = Color.Blue
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThumbnailCard(
    thumbnail: Thumbnail, onThumbnailClicked: (String) -> Unit, modifier: Modifier = Modifier
) {
    val haptics = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current
    var picture: Any
    try {
         picture = thumbnail.pictureString?.let { stringToBitmap(it) } ?: thumbnail.picture
    }
    catch (e: Exception) {
        picture = thumbnail.picture
        Toast.makeText(LocalContext.current, e.toString()+ thumbnail.postLink, Toast.LENGTH_SHORT).show()
    }
    println("LOG->CommonUi.kt: The current picture is: " + picture)
    Card(
        // User clicks on the thumbnail card.  Load full Isosta post on click.
        onClick = {onThumbnailClicked(thumbnail.postLink)},
        modifier = modifier
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(picture)
                    .crossfade(true)
                    .setHeader("User-Agent", "Mozilla/5.0")
                    .build(),
                error = painterResource(R.drawable.broken_image),
                placeholder = painterResource(R.drawable.hourglass_top),
                contentDescription = thumbnail.text,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = thumbnail.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp).combinedClickable(
                    onClick = {},
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        clipboardManager.setText(AnnotatedString(thumbnail.text))
                    }
                )
            )
        }
    }
}

@Composable
fun UserCard(
    user: IsostaUser,
    onUserButtonClicked: (IsostaUser) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .padding(5.dp)
                .clickable(
                    onClick = { onUserButtonClicked(user) }
                )
        ) {
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
            Column(modifier = Modifier
                .padding(10.dp)
                .weight(5f)) {
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
fun TextMessageScreenPreview() {
    TextMessageScreen(
        text = """
            FATAL EXCEPTION: main
            Process: io.github.azusalad.isosta, PID: 26421
            java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
                at java.util.ArrayList.get(ArrayList.java:437)
                                                                                           
        """.trimIndent()
    )
}
