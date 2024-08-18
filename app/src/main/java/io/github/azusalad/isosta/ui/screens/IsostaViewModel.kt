package io.github.azusalad.isosta.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import io.github.azusalad.isosta.IsostaApplication
import io.github.azusalad.isosta.data.IsostaPostRepository
import io.github.azusalad.isosta.data.IsostaUserRepository
import io.github.azusalad.isosta.data.ThumbnailRoomRepository
import io.github.azusalad.isosta.data.UserRoomRepository
import io.github.azusalad.isosta.model.IsostaPost
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.model.Thumbnail
import io.github.azusalad.isosta.ui.shareBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// The mutable interface stores the status of the most recent web request
sealed interface IsostaHomeUiState {
    object Display : IsostaHomeUiState
    data class Loading(val text: String) : IsostaHomeUiState
}

sealed interface IsostaPostUiState {
    data class Success(val isostaPost: IsostaPost) : IsostaPostUiState
    data class Error(val errorString: String) : IsostaPostUiState
    object Loading : IsostaPostUiState
}

sealed interface IsostaUserUiState {
    data class Success(
        val user: IsostaUser
    ) : IsostaUserUiState
    data class Error(val errorString: String) : IsostaUserUiState
    object Loading : IsostaUserUiState
}

sealed interface IsostaSearchUiState {
    data class Success(
        val userList: List<IsostaUser>
    ) : IsostaSearchUiState
    data class Error(val errorString: String) : IsostaSearchUiState
    object Empty : IsostaSearchUiState
    object Loading : IsostaSearchUiState
}

data class ThumbnailUiState(val thumbnailList: List<Thumbnail> = arrayListOf())
data class UserUiState(val userList: List<IsostaUser> = arrayListOf())

class IsostaViewModel(
    private val isostaPostRepository: IsostaPostRepository,
    private val isostaUserRepository: IsostaUserRepository,
    private val thumbnailRepository: ThumbnailRoomRepository,
    private val userRepository: UserRoomRepository
): ViewModel() {
    // Mutable state stores the status of the most recent request.  The initial state is loading.
    var isostaHomeUiState: IsostaHomeUiState by mutableStateOf(IsostaHomeUiState.Display)
        private set
        // ^ private setter
    var isostaPostUiState: IsostaPostUiState by mutableStateOf(IsostaPostUiState.Loading)
        private set
    var isostaUserUiState: IsostaUserUiState by mutableStateOf(IsostaUserUiState.Loading)
        private set
    var isostaSearchUiState: IsostaSearchUiState by mutableStateOf(IsostaSearchUiState.Empty)
        private set

    var thumbnailUiState: StateFlow<ThumbnailUiState> =
        thumbnailRepository.loadAllThumbnailStream().map { ThumbnailUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ThumbnailUiState()
            )
    var userUiState: StateFlow<UserUiState> =
        userRepository.loadAllUserStream().map { UserUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UserUiState()
            )

    init {
        //getThumbnailPhotos()  // TODO: Replace this placeholder

        // Placeholder
//        val yui = "https://avatars.githubusercontent.com/u/68360714?v=4"
//        val thumbnailList = arrayListOf<Thumbnail>()
//        thumbnailList.add(
//            Thumbnail(
//                picture = yui,
//                postLink = "",
//                text = "description 1"
//            )
//        )
//        isostaHomeUiState = IsostaHomeUiState.Success(thumbnailList)
    }

    fun getThumbnailPhotos(thumbnailViewModel: ThumbnailViewModel, context: Context, users: List<IsostaUser>, thumbnailUiState: ThumbnailUiState) {
        val delay = users.size.toLong() * 250  // Extra 250 ms delay per run for every additional user
        var index = 0
        viewModelScope.launch {
            // Might not be able to connect to website so need a try catch here.
            try {
                // The viewModelScope.launch launches on the main thread which leads to network
                // on main thread exception.  Use another dispatcher for background work.
                for (user in users) {
                    index++
                    isostaHomeUiState = IsostaHomeUiState.Loading(
                        text = "Fetching thumbnails...\n\n" +
                                index + "/" + users.size + "\n" +
                                user.profileHandle
                    )
                    println("LOG->IsostaViewModel.kt: Attempting to fetch website: " + user.profileLink)
                    withContext(Dispatchers.IO) {
                        // Use the repository to get the thumbnail photos
                        val result = isostaUserRepository.getUserInfo(user.profileLink)
                        thumbnailViewModel.saveThumbnails(context, result.thumbnailList!!)
                        delay(delay)
                        isostaHomeUiState = IsostaHomeUiState.Display
                    }
                }
                // This does not fix new thumbnails just fetched, but those thumbnails should
                // not have the problem anyway since the getUserInfo() function has been updated
                thumbnailViewModel.fixSlash(thumbnailUiState.thumbnailList)
            } catch (e: Exception) {  // Previously IOException
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                isostaHomeUiState = IsostaHomeUiState.Display
                println("LOG->IsostaViewModel.kt: There was an error fetching the website")
            }
        }

    }

    fun getPostInfo(url: String) {
        viewModelScope.launch {
            isostaPostUiState = IsostaPostUiState.Loading
            try {
                println("LOG: Attempting to fetch post")
                withContext(Dispatchers.IO) {
                    val result = isostaPostRepository.getPostInfo(url)
                    isostaPostUiState =
                        IsostaPostUiState.Success(result)
                }
            } catch (e: Exception) {  // Previously IOException
                isostaPostUiState = IsostaPostUiState.Error(e.toString())
                println("LOG->IsostaViewModel.kt: There was an error fetching the post")
                println("LOG->IsostaViewModel.kt: the error for fetching the post is " + e)
            }
        }
    }
    
    fun getUserInfo(url: String) {
        viewModelScope.launch {
            isostaUserUiState = IsostaUserUiState.Loading
            try {
                println("LOG->IsostaViewModel.kt: Attempting to fetch User")
                withContext(Dispatchers.IO) {
                    val result = isostaUserRepository.getUserInfo(url)
                    isostaUserUiState =
                        IsostaUserUiState.Success(result)
                }
            } catch (e: Exception) {  // Previously IOException
                isostaUserUiState = IsostaUserUiState.Error(e.toString())
                println("LOG->IsostaViewModel.kt: There was an error fetching the User")
                println("LOG->IsostaViewModel.kt: the error for fetching the User is " + e)
            }
        }
    }

    fun getSearchInfo(query: String) {
        viewModelScope.launch {
            isostaSearchUiState = IsostaSearchUiState.Loading
            try {
                println("LOG->IsostaViewModel.kt: Attempting to search with query: " + query)
                withContext(Dispatchers.IO) {
                    val result = isostaUserRepository.getSearchInfo(query)
                    isostaSearchUiState =
                        IsostaSearchUiState.Success(result)
                }
            } catch (e: Exception) {  // Previously IOException
                isostaSearchUiState = IsostaSearchUiState.Error(e.toString())
                println("LOG->IsostaViewModel.kt: There was an error searching with the query: " + query)
                println("LOG->IsostaViewModel.kt: the error for making the query is " + e)
            }
        }
    }

    // https://stackoverflow.com/questions/60910978/how-to-return-value-from-async-coroutine-scope-such-as-viewmodelscope-to-your-ui
    fun getBitmap(context: Context, request: ImageRequest) {
        viewModelScope.launch {
            val loader = ImageLoader(context)
            val result = (loader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap
            shareBitmap(context = context, bitmap = bitmap)
        }
    }

    // The viewmodel does not allow arguments to be passed in when the viewmodel is created.
    // Use a factory instead to solve this issue.
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IsostaApplication)
                val isostaPostRepository = application.container.isostaPostRepository
                val isostaUserRepository = application.container.isostaUserRepository
                val thumbnailRoomRepository = application.container.thumbnailRoomRepository
                val userRoomRepository = application.container.userRoomRepository

                IsostaViewModel(
                    isostaPostRepository = isostaPostRepository,
                    isostaUserRepository = isostaUserRepository,
                    thumbnailRepository = thumbnailRoomRepository,
                    userRepository = userRoomRepository
                )
            }
        }
    }
}
