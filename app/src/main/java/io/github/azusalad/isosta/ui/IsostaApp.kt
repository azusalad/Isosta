package io.github.azusalad.isosta.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import io.github.azusalad.isosta.ui.screens.HomeScreen
import io.github.azusalad.isosta.ui.screens.IsostaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.azusalad.isosta.R
import io.github.azusalad.isosta.model.IsostaComment
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.ui.screens.PostScreen
import io.github.azusalad.isosta.ui.screens.PostScreenPreview
import io.github.azusalad.isosta.ui.screens.UserColumnPreview
import io.github.azusalad.isosta.ui.screens.UserScreen

// Contains composables that render what the user would see in the Isosta App

// For the NavHost
enum class IsostaScreen(val title: String) {
    Home(title = "Isosta Home Screen"),
    Post(title = "Isosta Post Screen"),
    User(title = "Isosta User Screen")
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
                        },
                        onUserButtonClicked = { user: io.github.azusalad.isosta.model.IsostaUser ->
                            /* TODO: Call a function to load the user's page */
                            navController.navigate(IsostaScreen.User.name)
                            println("LOG: loading user page for " + user.profileLink)
                            isostaViewModel.getUserInfo(user.profileLink)
                        }
                    )
                }
                composable(route = IsostaScreen.User.name) {
                    UserScreen(
                        isostaUserUiState = isostaViewModel.isostaUserUiState,
                        onThumbnailClicked = { url: String ->
                            navController.navigate(IsostaScreen.Post.name)
                            println("LOG: getting post info for " + url)
                            isostaViewModel.getPostInfo(url)
                        }
                    )
                    //UserColumnPreview()
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
