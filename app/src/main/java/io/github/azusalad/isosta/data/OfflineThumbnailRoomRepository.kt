package io.github.azusalad.isosta.data

import io.github.azusalad.isosta.model.Thumbnail
import kotlinx.coroutines.flow.Flow

class OfflineThumbnailRoomRepository(private val thumbnailDao: ThumbnailDao) : ThumbnailRoomRepository {
    override fun loadAllThumbnailStream(): Flow<List<Thumbnail>> = thumbnailDao.loadAllThumbnail()

    override fun loadThumbnailStream(postLink: String): Flow<Thumbnail?> = thumbnailDao.loadThumbnail(postLink)

    override suspend fun insertThumbnail(thumbnail: Thumbnail) = thumbnailDao.insert(thumbnail)

    override suspend fun deleteThumbnail(thumbnail: Thumbnail) = thumbnailDao.delete(thumbnail)

    override suspend fun updateThumbnail(thumbnail: Thumbnail) = thumbnailDao.update(thumbnail)
}
