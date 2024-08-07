package io.github.azusalad.isosta.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.azusalad.isosta.IsostaApplication
import io.github.azusalad.isosta.data.IsostaPostRepository
import io.github.azusalad.isosta.data.IsostaUserRepository
import io.github.azusalad.isosta.model.IsostaPost
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.model.Thumbnail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// The mutable interface stores the status of the most recent web request
sealed interface IsostaHomeUiState {
    data class Success(val thumbnailPhotos: List<Thumbnail>) : IsostaHomeUiState
    data class Error(val errorString: String) : IsostaHomeUiState
    object Empty : IsostaHomeUiState
    object Loading : IsostaHomeUiState
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

class IsostaViewModel(
    private val isostaPostRepository: IsostaPostRepository,
    private val isostaUserRepository: IsostaUserRepository
): ViewModel() {
    // Mutable state stores the status of the most recent request.  The initial state is loading.
    var isostaHomeUiState: IsostaHomeUiState by mutableStateOf(IsostaHomeUiState.Empty)
        private set
        // ^ private setter
    var isostaPostUiState: IsostaPostUiState by mutableStateOf(IsostaPostUiState.Loading)
        private set
    var isostaUserUiState: IsostaUserUiState by mutableStateOf(IsostaUserUiState.Loading)
        private set
    var isostaSearchUiState: IsostaSearchUiState by mutableStateOf(IsostaSearchUiState.Empty)
        private set

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

    fun getThumbnailPhotos(url: String = "https://imginn.com/suisei.daily.post/") {
        viewModelScope.launch {
            isostaHomeUiState = IsostaHomeUiState.Loading
            // Might not be able to connect to website so need a try catch here.
            try {
                // The viewModelScope.launch launches on the main thread which leads to network
                // on main thread exception.  Use another dispatcher for background work.
                println("LOG: Attempting to fetch website")
                withContext(Dispatchers.IO) {
                    // Use the repository to get the thumbnail photos
                    val result = isostaUserRepository.getUserInfo(url)
                    isostaHomeUiState =
                        IsostaHomeUiState.Success(result.thumbnailList)
                }
            } catch (e:Exception) {  // Previously IOException
                isostaHomeUiState = IsostaHomeUiState.Error(e.toString())
                println("LOG: There was an error fetching the website")
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
                println("LOG: There was an error fetching the post")
                println("LOG: the error for fetching the post is " + e)
            }
        }
    }
    
    fun getUserInfo(url: String) {
        viewModelScope.launch {
            isostaUserUiState = IsostaUserUiState.Loading
            try {
                println("LOG: Attempting to fetch User")
                withContext(Dispatchers.IO) {
                    val result = isostaUserRepository.getUserInfo(url)
                    isostaUserUiState =
                        IsostaUserUiState.Success(result)
                }
            } catch (e: Exception) {  // Previously IOException
                isostaUserUiState = IsostaUserUiState.Error(e.toString())
                println("LOG: There was an error fetching the User")
                println("LOG: the error for fetching the User is " + e)
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

    // The viewmodel does not allow arguments to be passed in when the viewmodel is created.
    // Use a factory instead to solve this issue.
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IsostaApplication)
                val isostaPostRepository = application.container.isostaPostRepository
                val isostaUserRepository = application.container.isostaUserRepository

                IsostaViewModel(
                    isostaPostRepository = isostaPostRepository,
                    isostaUserRepository = isostaUserRepository
                )
            }
        }
    }
}
