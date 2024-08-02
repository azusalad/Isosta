package com.example.isosta.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import com.example.isosta.ui.screens.HomeScreen
import com.example.isosta.ui.screens.IsostaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.isosta.R
import com.example.isosta.model.IsostaComment
import com.example.isosta.ui.screens.PostScreen
import com.example.isosta.ui.screens.PostScreenPreview

// Contains composables that render what the user would see in the Isosta App

// For the NavHost
enum class IsostaScreen() {
    Home,
    Post
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsostaApp(
    navController: NavHostController = rememberNavController()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { IsostaTopAppBar(scrollBehavior = scrollBehavior) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val isostaViewModel: IsostaViewModel = viewModel(factory = IsostaViewModel.Factory)
            NavHost(
                navController = navController,
                startDestination = IsostaScreen.Home.name,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(route = IsostaScreen.Home.name) {
                    // The main home screen and feed
                    HomeScreen(
                        isostaUiState = isostaViewModel.isostaUiState,
                        onThumbnailClicked = {
                            // it is the url to go to
                            //isostaViewModel.pageState.postLink = it
                            navController.navigate(IsostaScreen.Post.name)
                            println("LOG: getting post info for " + it)
                            isostaViewModel.getPostInfo(it)
                        }
                        // The content padding here is not needed anymore due to the padding being
                        // put on the NavHost
                        //contentPadding = paddingValues,
                    )
                }
                composable(route = IsostaScreen.Post.name) {
                    // The screen that shows when the user clicks on a ThumbnailCard
                    //PostScreenPreview()
                    val context = LocalContext.current
                    PostScreen(
                        isostaPostUiState = isostaViewModel.isostaPostUiState,
                        onShareButtonClicked = { postLink: String ->
                            sharePost(context = context, postLink = postLink)
                        }
                    )
//                    val mediaList = arrayListOf<Int>()
//                    mediaList.add(R.drawable.broken_image)
//                    mediaList.add(R.drawable.hourglass_top)
//                    mediaList.add(R.drawable.ic_launcher_background)
//                    mediaList.add(R.drawable.ic_launcher_foreground)
//
//                    val commentList = arrayListOf<IsostaComment>()
//                    commentList.add(IsostaComment(R.drawable.broken_image, "Broken image", "Comment"))
//                    commentList.add(IsostaComment(R.drawable.hourglass_top, "Hourglass", "Comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment comment commentv"))
//                    commentList.add(IsostaComment(R.drawable.ic_launcher_background, "Launcher background", "Comment"))
//                    PostScreen(
//                        isostaUiState = isostaViewModel.isostaUiState,
//                        profilePicture = R.drawable.ic_launcher_background,
//                        profileName = "Profile Name",
//                        profileHandle = "@profilehandle",
//                        postDescription = "Post description",
//                        mediaList = mediaList,
//                        commentList = commentList,
//                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsostaTopAppBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "Isosta Home Screen",
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        modifier = modifier
    )
}

private fun sharePost(context: Context, postLink: String) {
    val shareIntent: Intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, postLink)
    }
    context.startActivity(
        Intent.createChooser(
            shareIntent,
            "Share this post"
        )
    )
}
