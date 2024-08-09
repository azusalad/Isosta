package io.github.azusalad.isosta.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import io.github.azusalad.isosta.data.ThumbnailRoomRepository
import io.github.azusalad.isosta.model.Thumbnail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ThumbnailViewModel(private val thumbnailRoomRepository: ThumbnailRoomRepository) : ViewModel() {

    private fun validateInput(thumbnail: Thumbnail): Boolean {
        return thumbnail.postLink.isNotBlank() && thumbnail.picture.isNotBlank() && thumbnail.sourceHandle.isNotBlank() && thumbnail.pictureString != null
    }

    private suspend fun saveThumbnail(context: Context, thumbnail: Thumbnail) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(thumbnail.picture)
            .setHeader("User-Agent", "Mozilla/5.0")
            .allowHardware(false)
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap
        thumbnail.pictureString = bitmapToString(bitmap)
        if (validateInput(thumbnail)) {
            println("LOG->ThumbnailViewModel.kt: Saving thumbnail " + thumbnail.postLink)
            thumbnailRoomRepository.insertThumbnail(thumbnail)
        }
    }

    fun saveThumbnails(context: Context, thumbnailList: List<Thumbnail>) {
        viewModelScope.launch {
            for (thumbnail in thumbnailList) {
                saveThumbnail(context = context, thumbnail = thumbnail)
            }
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

// https://medium.com/@sh0707.lee/the-easiest-method-to-save-images-in-room-database-by-base64-encoding-9b697e47b6fa
fun bitmapToString(bitmap: Bitmap): String {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
    val byteArray = baos.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun stringToBitmap(byteArray: String): Bitmap {
    val encodeByte = Base64.decode(byteArray, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
}
