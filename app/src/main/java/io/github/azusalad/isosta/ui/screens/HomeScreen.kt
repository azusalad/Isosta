package io.github.azusalad.isosta.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.azusalad.isosta.R
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.model.Thumbnail
import io.github.azusalad.isosta.ui.components.TextMessageScreen
import io.github.azusalad.isosta.ui.components.ThumbnailCard
import io.github.azusalad.isosta.ui.components.UserCard
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    isostaHomeUiState: IsostaHomeUiState,
    onThumbnailClicked: (String) -> Unit,
    onUserButtonClicked: (IsostaUser) -> Unit,
    onSearchButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    // TODO: Remove this placeholder and put actual follow list in
    val yui = "https://avatars.githubusercontent.com/u/68360714?v=4"
    val userList = arrayListOf<IsostaUser>()
    userList.add(
        IsostaUser(
            profilePicture = yui,
            profileName = "Yui",
            profileHandle = "@yui",
            profileLink = "https://imginn.com/suisei.daily.post/",
        )
    )
    userList.add(
        IsostaUser(
            profilePicture = yui,
            profileName = "Yui2",
            profileHandle = "@yui2",
            profileLink = "https://imginn.com/suisei.daily.post/",
        )
    )

    // Switch statement shows different things depending on the IsostaUiState
    when (isostaHomeUiState) {
        is IsostaHomeUiState.Loading -> TextMessageScreen(text = "Loading thumbnails", modifier = modifier.fillMaxSize())
        is IsostaHomeUiState.Success -> HomePager(
            thumbnailList = isostaHomeUiState.thumbnailPhotos,
            userList = userList,
            onThumbnailClicked = onThumbnailClicked,
            onUserButtonClicked = onUserButtonClicked,
            onSearchButtonClicked = onSearchButtonClicked,
            modifier = modifier.fillMaxWidth(),
            contentPadding = contentPadding,
        )
        //is IsostaUiState.Success -> TextMessageScreen(text = isostaUiState.thumbnailPhotos, modifier = modifier.fillMaxWidth())
        is IsostaHomeUiState.Error -> TextMessageScreen(
            text = "There was a error loading thumbnails:\n\n" + isostaHomeUiState.errorString,
            modifier = modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePager(
    thumbnailList: List<Thumbnail>,
    userList: List<IsostaUser>,
    onThumbnailClicked: (String) -> Unit,
    onUserButtonClicked: (IsostaUser) -> Unit,
    onSearchButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val pages = arrayListOf<String>()
    pages.add("Home Feed ")
    pages.add("Following")
    val pagerState = rememberPagerState(pageCount = {pages.size})
    val scrollCoroutineScope = rememberCoroutineScope()
    // Horizontal pager with tabs
    // https://medium.com/@domen.lanisnik/exploring-the-official-pager-in-compose-8c2698c49a98
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                )
            }
        ) {
            pages.forEachIndexed { index, title ->
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable {
                            scrollCoroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                        .padding(8.dp)
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (page == 0) {
                // First page is main feed
                ThumbnailList(
                    thumbnailList = thumbnailList,
                    onThumbnailClicked = onThumbnailClicked,
                    modifier = modifier.fillMaxWidth(),
                    contentPadding = contentPadding,
                )
            }
            else {
                // Second page is follow column
                FollowList(
                    userList = userList,
                    onUserButtonClicked = onUserButtonClicked
                )
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ExtendedFloatingActionButton(
            onClick = onSearchButtonClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            text = { Text(text = "Search") },
            modifier = Modifier.padding(24.dp).align(Alignment.BottomEnd)
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
fun FollowList(
    userList: List<IsostaUser>,
    onUserButtonClicked: (IsostaUser) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(contentPadding = contentPadding, modifier = modifier) {
        items(userList) { isostaUser ->
            UserCard(
                user = isostaUser,
                onUserButtonClicked = onUserButtonClicked
            )
            Spacer(modifier = Modifier.height(20.dp))
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
        userList = userList,
        onUserButtonClicked = {}
    )
}

@Preview
@Composable
fun HomePagerPreview() {
    val yui = "https://avatars.githubusercontent.com/u/68360714?v=4"
    val userList = arrayListOf<IsostaUser>()
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
    HomePager(
        thumbnailList = thumbnailList,
        userList = userList,
        onThumbnailClicked = {},
        onUserButtonClicked = {},
        onSearchButtonClicked = {}
    )
}
