package com.example.isosta.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.isosta.R
import com.example.isosta.model.IsostaComment
import com.example.isosta.model.Thumbnail

// Show this screen when a ThumbnailCard is clicked.
@Composable
fun PostScreen(
    profilePicture: Int,
    profileName: String,
    profileHandle: String,
    postDescription: String,
    mediaList: List<Int>,
    commentList: List<IsostaComment>,
    modifier: Modifier = Modifier
) {
//    val mediaList = arrayListOf<Int>()
//    mediaList.add(R.drawable.d)
//    mediaList.add(R.drawable.c)
//    mediaList.add(R.drawable.b)
//    mediaList.add(R.drawable.a)
//
//    val commentList = arrayListOf<IsostaComment>()
//    commentList.add(IsostaComment(R.drawable.a, "Reona", "Lmao bruh comment"))
//    commentList.add(IsostaComment(R.drawable.d, "Yui", "Big ass paragraph Big ass paragraph Big ass paragraph Big ass paragraph Big ass paragraph Big ass paragraph Big ass paragraph Big ass paragraph Big ass paragraph Big ass paragraph Big ass paragraph Big ass paragraph Big ass paragraph "))
//    commentList.add(IsostaComment(R.drawable.c, "Kita", "Comment"))

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            PostCard(
                profilePicture = profilePicture,
                profileName = profileName,
                profileHandle = profileHandle,
                postDescription = postDescription,
                mediaList = mediaList
            )
        }
        item {
            Text(
                text = "Comments",
                style = MaterialTheme.typography.headlineLarge,
                modifier = modifier.padding(10.dp)
            )
        }
        items(commentList) { commentInformation ->
            CommentCard(isostaComment = commentInformation)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// The entire card composable containing the post images, description, author information, etc
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostCard(
    profilePicture: Int,
    profileName: String,
    profileHandle: String,
    postDescription: String,
    mediaList: List<Int>,
    modifier: Modifier = Modifier
) {
    val haptics = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current
    Card(modifier = modifier) {
        Column {
            AuthorInformation(
                profilePicture = profilePicture,
                profileName = profileName,
                profileHandle = profileHandle,
                onClick = {/* TODO: Create onClick */}
            )
            MediaPager(mediaList)
            Text(
                // Description text
                text = postDescription,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp).combinedClickable(
                    onClick = {},
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        clipboardManager.setText(AnnotatedString(postDescription))
                    }
                )
            )
        }
    }
}

// Top author information with profile picture.
@Composable
fun AuthorInformation(
    profilePicture: Int,
    profileName: String,
    profileHandle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().padding(5.dp).clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(profilePicture),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clip(CircleShape)
            )
        }
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.weight(5f)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = profileName,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(
                    text = profileHandle,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().weight(1f)
        ) {
            Image(
                painter = painterResource(R.drawable.share),
                contentDescription = "Share icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clickable(
                    onClick = {/* TODO: Share intent for post link */}
                )
            )
        }
    }
}

// Contains the post images in a pager and the pager indicators
// https://bootcamp.uxdesign.cc/improving-compose-horizontal-pager-indicator-bcf3b67835a
// https://developer.android.com/develop/ui/compose/layouts/pager
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPager(
    mediaList: List<Int>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // Show all the media in this post in a horizontal pager
    val pagerState = rememberPagerState(pageCount = {
        mediaList.size
    })
    Box(
        // Box contains the post images as a pager and the pager indicators
        //verticalArrangement = Arrangement.Center,
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxWidth().aspectRatio(1f)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // Pager content
            Image(
                painter = painterResource(mediaList[page]),
                contentDescription = "BRUH",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().clickable(
                    onClick = {/* TODO */},
                )
            )

        }
        Row(
            // Page indicators
            modifier = Modifier
                // Adjust the position of the indicators with the height
                .height(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(mediaList.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(color, CircleShape)
                        .size(10.dp)
                )
            }
        }

    }
}

// Card composable for each comment
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentCard(
    isostaComment: IsostaComment,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    Card(modifier = modifier.fillMaxWidth()) {
        Row (modifier = modifier.padding(5.dp)) {
            // Profile Picture
            Image(
                painter = painterResource(isostaComment.profilePicture),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f/1f)
                    .clip(CircleShape)
                    .weight(1f)
                    .clickable(
                        onClick = {/* TODO: Open author page */}
                    )
            )
            Column(modifier = Modifier.padding(10.dp).weight(5f)) {
                // Profile name and comment text
                Text(
                    text = isostaComment.profileName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.clickable(
                        onClick = {/* TODO: Open author page */}
                    )
                )
                Text(
                    text = isostaComment.commentText,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.combinedClickable(
                        onClick = {},
                        onLongClick = {
                            clipboardManager.setText(AnnotatedString(isostaComment.commentText))
                        }
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun PostScreenPreview() {
    val mediaList = arrayListOf<Int>()
    mediaList.add(R.drawable.broken_image)
    mediaList.add(R.drawable.hourglass_top)
    mediaList.add(R.drawable.ic_launcher_background)
    mediaList.add(R.drawable.ic_launcher_foreground)

    val commentList = arrayListOf<IsostaComment>()
    commentList.add(IsostaComment(R.drawable.broken_image, "Broken image", "Comment"))
    commentList.add(IsostaComment(R.drawable.hourglass_top, "Hourglass", "Comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment commentv"))
    commentList.add(IsostaComment(R.drawable.ic_launcher_background, "Launcher background", "Comment"))
    PostScreen(
        profilePicture = R.drawable.ic_launcher_background,
        profileName = "Profile Name",
        profileHandle = "@profilehandle",
        postDescription = "Post description",
        mediaList = mediaList,
        commentList = commentList,
    )
}
