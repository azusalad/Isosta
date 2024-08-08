package io.github.azusalad.isosta.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.azusalad.isosta.IsostaApplication
import io.github.azusalad.isosta.model.Thumbnail
import io.github.azusalad.isosta.data.ThumbnailRoomRepository

class ThumbnailViewModel(private val thumbnailRoomRepository: ThumbnailRoomRepository) : ViewModel() {
    var thumbnailUiState by mutableStateOf(ThumbnailUiState())
        private set

    fun updateUiState(thumbnailDetails: ThumbnailDetails) {
        thumbnailUiState =
            ThumbnailUiState(thumbnailDetails = thumbnailDetails, isEntryValid = validateInput(thumbnailDetails))
    }

    private fun validateInput(uiState: ThumbnailDetails = thumbnailUiState.thumbnailDetails): Boolean {
        return with(uiState) {
            postLink.isNotBlank() && picture.isNotBlank() && sourceHandle.isNotBlank()
        }
    }

    suspend fun saveThumbnail(thumbnail: Thumbnail) {
        if (validateInput(thumbnail.toThumbnailDetails())) {
            println("LOG->ThumbnailViewModel.kt: Saving thumbnail " + thumbnail.postLink)
            thumbnailRoomRepository.insertThumbnail(thumbnail)
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IsostaApplication)

                ThumbnailViewModel(
                    application.container.thumbnailRoomRepository
                )
            }
        }
    }
}

data class ThumbnailUiState(
    val thumbnailDetails: ThumbnailDetails = ThumbnailDetails(),
    val isEntryValid: Boolean = false
)

data class ThumbnailDetails(
    val postLink: String = "",
    val picture: String = "",
    val text: String = "",
    val sourceHandle: String = "",
    val date: Int = 0,
)

// Details to Thumbnail
fun ThumbnailDetails.toThumbnail(): Thumbnail = Thumbnail(
    postLink = postLink,
    picture = picture,
    text = text,
    sourceHandle = sourceHandle,
    date = date
)

// Thumbnail to ThumbnailUiState
fun Thumbnail.ThumbnailUiState(isEntryValid: Boolean = false): ThumbnailUiState = ThumbnailUiState(
    thumbnailDetails = this.toThumbnailDetails(),
    isEntryValid = isEntryValid
)

// Thumbnail to Details
fun Thumbnail.toThumbnailDetails(): ThumbnailDetails = ThumbnailDetails(
    postLink = postLink,
    picture = picture,
    text = text,
    sourceHandle = sourceHandle,
    date = date
)

