package com.example.isosta.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.isosta.R
import com.example.isosta.model.Thumbnail

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
fun ThumbnailCard(
    thumbnail: Thumbnail, onThumbnailClicked: (String) -> Unit, modifier: Modifier = Modifier
) {
    println("LOG: The current picture is: " + thumbnail.picture)
    Card(
        // User clicks on the thumbnail card.  Load full Isosta post on click.
        onClick = {onThumbnailClicked(thumbnail.postLink)},
        modifier = modifier
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(thumbnail.picture)
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
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Preview
@Composable
fun TextMessageScreenPreview() {
    TextMessageScreen(
        text = """
            FATAL EXCEPTION: main
            Process: com.example.isosta, PID: 26421
            java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
                at java.util.ArrayList.get(ArrayList.java:437)
                                                                                           
        """.trimIndent()
    )
}
