package io.github.azusalad.isosta.data

import io.github.azusalad.isosta.model.Thumbnail
import kotlinx.coroutines.flow.Flow

interface ThumbnailRoomRepository {

    fun loadAllThumbnailStream(): Flow<List<Thumbnail>>

    fun loadThumbnailStream(postLink: String): Flow<Thumbnail?>

    suspend fun insertThumbnail(thumbnail: Thumbnail)

    suspend fun deleteThumbnail(thumbnail: Thumbnail)

    suspend fun updateThumbnail(thumbnail: Thumbnail)

}
