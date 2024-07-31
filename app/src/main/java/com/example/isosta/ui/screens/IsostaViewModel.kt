package com.example.isosta.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.isosta.IsostaThumbnailsApplication
import com.example.isosta.data.IsostaPostRepository
import com.example.isosta.data.IsostaThumbnailsRepository
import com.example.isosta.data.NetworkIsostaThumbnailsRepository
import com.example.isosta.model.IsostaPost
import com.example.isosta.model.Thumbnail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

// The mutable interface stores the status of the most recent web request
sealed interface IsostaUiState {
    data class Success(val thumbnailPhotos: List<Thumbnail>) : IsostaUiState
    object Error : IsostaUiState
    object Loading : IsostaUiState
}

sealed interface IsostaPostUiState {
    data class Success(val isostaPost: IsostaPost) : IsostaPostUiState
    object Error : IsostaPostUiState
    object Loading : IsostaPostUiState
}

data class PageState(
    var postLink: String,
    var userLink: String
)

class IsostaViewModel(
    private val isostaThumbnailsRepository: IsostaThumbnailsRepository,
    private val isostaPostRepository: IsostaPostRepository
): ViewModel() {
    // Mutable state stores the status of the most recent request.  The initial state is loading.
    var isostaUiState: IsostaUiState by mutableStateOf(IsostaUiState.Loading)
        private set
        // ^ private setter
    var isostaPostUiState: IsostaPostUiState by mutableStateOf(IsostaPostUiState.Loading)
        private set

    var pageState = PageState(postLink = "", userLink = "")

    init {
        getThumbnailPhotos()
    }

    fun getThumbnailPhotos() {
        viewModelScope.launch {
            isostaUiState = IsostaUiState.Loading
            // Might not be able to connect to website so need a try catch here.
            try {
                // The viewModelScope.launch launches on the main thread which leads to network
                // on main thread exception.  Use another dispatcher for background work.
                println("LOG: Attempting to fetch website")
                withContext(Dispatchers.IO) {
                    // Use the repository to get the thumbnail photos
//                    val listResult = isostaThumbnailsRepository.getThumbnailPhotos()
//                    isostaUiState =
//                        IsostaUiState.Success("Success: ${listResult.size} thumbnails received")
                    val result = isostaThumbnailsRepository.getThumbnailPhotos()
                    isostaUiState =
                        //IsostaUiState.Success("First Isosta image URL: ${result.picture}")
                        IsostaUiState.Success(result)
                }
            } catch (e:IOException) {
                IsostaUiState.Error
                println("LOG: There was an error fetching the website")
            }
        }
    }

    fun getPostInfo() {
        viewModelScope.launch {
            isostaPostUiState = IsostaPostUiState.Loading
            try {
                println("LOG: Attempting to fetch post")
                withContext(Dispatchers.IO) {
                    val result = isostaPostRepository.getPostInfo()
                    isostaPostUiState =
                        IsostaPostUiState.Success(result)
                }
            } catch (e:IOException) {
                IsostaPostUiState.Error
                println("LOG: There was an error fetching the post")
                println("LOG: the error for fetching the post is " + e)
            }
        }
    }

    // The viewmodel does not allow arguments to be passed in when the viewmodel is created.
    // Use a factory instead to solve this issue.
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IsostaThumbnailsApplication)
                val isostaThumbnailsRepository = application.container.isostaThumbnailsRepository
                val isostaPostRepository = application.container.isostaPostRepository
                IsostaViewModel(isostaThumbnailsRepository = isostaThumbnailsRepository, isostaPostRepository = isostaPostRepository)
            }
        }
    }
}
