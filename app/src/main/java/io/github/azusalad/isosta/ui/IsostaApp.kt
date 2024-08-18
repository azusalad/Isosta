package io.github.azusalad.isosta.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import io.github.azusalad.isosta.ui.screens.HomeScreen
import io.github.azusalad.isosta.ui.screens.IsostaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.ui.screens.PostScreen
import io.github.azusalad.isosta.ui.screens.SearchScreen
import io.github.azusalad.isosta.ui.screens.SearchScreenPreview
import io.github.azusalad.isosta.ui.screens.ThumbnailViewModel
import io.github.azusalad.isosta.ui.screens.UserScreen
import io.github.azusalad.isosta.ui.screens.UserViewModel
import java.io.File
import java.io.FileOutputStream
import io.github.azusalad.isosta.BuildConfig

// Contains composables that render what the user would see in the Isosta App

// For the NavHost
enum class IsostaScreen(val title: String) {
    Home(title = "Isosta Home Screen"),
    Post(title = "Isosta Post Screen"),
    User(title = "Isosta User Screen"),
    Search(title = "Isosta Search Screen")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsostaApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = IsostaScreen.valueOf(
        backStackEntry?.destination?.route ?: IsostaScreen.Home.name
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { IsostaTopAppBar(currentScreen = currentScreen, scrollBehavior = scrollBehavior) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val isostaViewModel: IsostaViewModel = viewModel(factory = IsostaViewModel.Factory)
            val thumbnailViewModel: ThumbnailViewModel = viewModel(factory = ThumbnailViewModel.Factory)
            val userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
            val thumbnailUiState by isostaViewModel.thumbnailUiState.collectAsState()
            val userUiState by isostaViewModel.userUiState.collectAsState()
            NavHost(
                navController = navController,
                startDestination = IsostaScreen.Home.name,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(route = IsostaScreen.Home.name) {
                    val context = LocalContext.current
                    // The main home screen and feed
                    HomeScreen(
                        isostaHomeUiState = isostaViewModel.isostaHomeUiState,
                        thumbnailUiState = thumbnailUiState,
                        userUiState = userUiState,
                        onThumbnailClicked = {
                            // it is the url to go to
                            //isostaViewModel.pageState.postLink = it
                            navController.navigate(IsostaScreen.Post.name)
                            println("LOG->IsostaApp.kt: getting post info for " + it)
                            isostaViewModel.getPostInfo(it)
                        },
                        onUserButtonClicked = { user: IsostaUser ->
                            navController.navigate(IsostaScreen.User.name)
                            println("LOG->IsostaApp.kt: loading user page for " + user.profileLink)
                            isostaViewModel.getUserInfo(user.profileLink)
                        },
                        onSearchButtonClicked = {
                            navController.navigate(IsostaScreen.Search.name)
                            println("LOG->IsostaApp.kt: Navigating to the search page")
                        },
                        onRefreshButtonClicked = {
                            println("LOG->IsostaApp.kt: Refresh button clicked")
                            isostaViewModel.getThumbnailPhotos(
                                    thumbnailViewModel = thumbnailViewModel,
                                    context = context,
                                    users = userUiState.userList,
                                    thumbnailUiState = thumbnailUiState
                                )
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
                        },
                        onMediaHeld = { request: ImageRequest ->
                            isostaViewModel.getBitmap(context, request)
                        },
                        onUserButtonClicked = { user: IsostaUser ->
                            navController.navigate(IsostaScreen.User.name)
                            println("LOG->IsostaApp.kt: loading user page for " + user.profileLink)
                            isostaViewModel.getUserInfo(user.profileLink)
                        }
                    )
                }
                composable(route = IsostaScreen.User.name) {
                    val context = LocalContext.current
                    UserScreen(
                        isostaUserUiState = isostaViewModel.isostaUserUiState,
                        userUiState = userUiState,
                        onThumbnailClicked = { url: String ->
                            navController.navigate(IsostaScreen.Post.name)
                            println("LOG->IsostaApp.kt: getting post info for " + url)
                            isostaViewModel.getPostInfo(url)
                        },
                        onFollowClicked = { user: IsostaUser ->
                            userViewModel.saveUser(context = context, user = user)
//                            thumbnailViewModel.saveThumbnails(context = context, thumbnailList = user.thumbnailList!!)
                            Toast.makeText(context, "Following user...", Toast.LENGTH_SHORT).show()
                        },
                        onUnfollowClicked = { user: IsostaUser ->
                            userViewModel.deleteUser(user = user)
                            Toast.makeText(context, "Unfollowing user...", Toast.LENGTH_SHORT).show()
                        },
                    )
                    //UserColumnPreview()
                }
                composable(route = IsostaScreen.Search.name) {
                    SearchScreen(
                        isostaSearchUiState = isostaViewModel.isostaSearchUiState,
                        onUserButtonClicked = { user: IsostaUser ->
                            navController.navigate(IsostaScreen.User.name)
                            println("LOG->IsostaApp.kt: loading user page for " + user.profileLink)
                            isostaViewModel.getUserInfo(user.profileLink)
                        },
                        onKeyboardDone = { query: String ->
                            println("LOG->IsostaApp.kt: Query made: " + query)
                            isostaViewModel.getSearchInfo(query)
                        }
                    )
                    //SearchScreenPreview()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsostaTopAppBar(
    currentScreen: IsostaScreen,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = currentScreen.title,
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

// https://stackoverflow.com/questions/72887876/convert-jetpack-compose-image-into-bitmap-image-for-sharing-purpose
fun shareBitmap(context: Context, bitmap: Bitmap) {
    // Write the bitmap to a file
    try {
        val file = try {
            val outputFile = File(context.cacheDir, "share.png")
            val fos = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            outputFile
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            return
        }
        // Get uri for the file and then open share sheet with the uri
        // https://stackoverflow.com/questions/56598480/couldnt-find-meta-data-for-provider-with-authority
        // https://stackoverflow.com/questions/42516126/fileprovider-illegalargumentexception-failed-to-find-configured-root
        val uri =
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                "Share picture"
            )
        )
    }
    catch (e: Exception) {
        println("LOG->IsostaApp.kt: " + e.toString())
        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
    }
}
