package io.github.azusalad.isosta.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
