package io.github.azusalad.isosta.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
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
import io.github.azusalad.isosta.data.UserRoomRepository
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.model.Thumbnail
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class UserViewModel(private val userRoomRepository: UserRoomRepository) : ViewModel() {

    private fun validateInput(user: IsostaUser): Boolean {
        return user.profileHandle.isNotBlank() && user.profileLink.isNotBlank() && user.pictureString != null
    }

    fun saveUser(context: Context, user: IsostaUser) {
        viewModelScope.launch {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(user.profilePicture)
                .setHeader("User-Agent", "Mozilla/5.0")
                .allowHardware(false)
                .build()

            val result = (loader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap
            user.pictureString = bitmapToString(bitmap)
            if (validateInput(user)) {
                println("LOG->ThumbnailViewModel.kt: Saving user " + user.profileHandle)
                userRoomRepository.insertUser(user)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IsostaApplication)

                UserViewModel(
                    application.container.userRoomRepository
                )
            }
        }
    }
}

