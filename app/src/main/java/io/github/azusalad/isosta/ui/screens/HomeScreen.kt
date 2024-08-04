package io.github.azusalad.isosta.ui.screens

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
import io.github.azusalad.isosta.R
import io.github.azusalad.isosta.model.Thumbnail
import io.github.azusalad.isosta.ui.components.TextMessageScreen
import io.github.azusalad.isosta.ui.components.ThumbnailList


@Composable
fun HomeScreen(
    isostaUiState: IsostaUiState,
    onThumbnailClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    // Switch statement shows different things depending on the IsostaUiState
    when (isostaUiState) {
        is IsostaUiState.Loading -> TextMessageScreen(text = "Loading thumbnails", modifier = modifier.fillMaxSize())
        is IsostaUiState.Success -> ThumbnailList(
            thumbnailList = isostaUiState.thumbnailPhotos, onThumbnailClicked = onThumbnailClicked, modifier = modifier.fillMaxWidth(), contentPadding = contentPadding,
        )
        //is IsostaUiState.Success -> TextMessageScreen(text = isostaUiState.thumbnailPhotos, modifier = modifier.fillMaxWidth())
        is IsostaUiState.Error -> TextMessageScreen(
            text = "There was a error loading thumbnails:\n\n" + isostaUiState.errorString,
            modifier = modifier.fillMaxSize())
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
    //ThumbnailCard(thumbnail = Thumbnail(picture = picture, text = "Mars photo", postLink = "", onThumbnailClicked = {}))
}
