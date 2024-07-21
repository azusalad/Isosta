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
import com.example.isosta.data.IsostaThumbnailsRepository
import com.example.isosta.data.NetworkIsostaThumbnailsRepository
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

class IsostaViewModel(
    private val isostaThumbnailsRepository: IsostaThumbnailsRepository
): ViewModel() {
    // Mutable state stores the status of the most recent request.  The initial state is loading.
    var isostaUiState: IsostaUiState by mutableStateOf(IsostaUiState.Loading)
        private set
        // ^ private setter

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

    // The viewmodel does not allow arguments to be passed in when the viewmodel is created.
    // Use a factory instead to solve this issue.
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IsostaThumbnailsApplication)
                val isostaThumbnailsRepository = application.container.isostaThumbnailsRepository
                IsostaViewModel(isostaThumbnailsRepository = isostaThumbnailsRepository)
            }
        }
    }
}
