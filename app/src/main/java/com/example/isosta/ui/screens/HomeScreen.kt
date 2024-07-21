package com.example.isosta.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun HomeScreen(
    isostaUiState: IsostaUiState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    // Switch statement shows different things depending on the IsostaUiState
    when (isostaUiState) {
        is IsostaUiState.Loading -> TextMessageScreen(text = "Loading thumbnails", modifier = modifier.fillMaxSize())
        is IsostaUiState.Success -> ThumbnailList(
            thumbnailList = isostaUiState.thumbnailPhotos, modifier = modifier.fillMaxWidth(), contentPadding = contentPadding,
        )
        //is IsostaUiState.Success -> TextMessageScreen(text = isostaUiState.thumbnailPhotos, modifier = modifier.fillMaxWidth())
        is IsostaUiState.Error -> TextMessageScreen(text = "There was a error loading thumbnails", modifier = modifier.fillMaxSize())
    }
}

@Composable
fun ThumbnailList(
    thumbnailList: List<Thumbnail>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(contentPadding = contentPadding, modifier = modifier) {
        items(thumbnailList) { thumbnail ->
            ThumbnailCard(
                thumbnail = thumbnail,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ThumbnailCard(thumbnail: Thumbnail, modifier: Modifier = Modifier) {
    println("LOG: The current picture is: " + thumbnail.picture)
    val picture = """
        https://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg
        """
    Card(
        // User clicks on the thumbnail card.  Load full Isosta post on click.
        onClick = {},
        modifier = modifier
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(thumbnail.picture)
                    //.data("https://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg")
                    .crossfade(true)
                    .setHeader("User-Agent", "Mozilla/5.0")
                    .build(),
                error = painterResource(R.drawable.broken_image),
                placeholder = painterResource(R.drawable.hourglass_top),
                contentDescription = thumbnail.text,
                modifier = Modifier
                    //.height(194.dp)
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

@Composable
fun TextMessageScreen(text: String, modifier: Modifier = Modifier) {
    println("LOG: Loading text message screen with the text: " + text)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 75.sp,
            lineHeight = 75.sp,
            color = Color.Blue
        )
    }
}

//@Preview
@Composable
fun ErrorPreview() {
    TextMessageScreen(text = "There was a error", modifier = Modifier.fillMaxSize())
}

@Preview
@Composable
fun ThumbnailCardPreview() {
    val picture = """
        https://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg
        """
    ThumbnailCard(thumbnail = Thumbnail(picture = picture, text = "Mars photo"))
}
