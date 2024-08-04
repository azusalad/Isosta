package io.github.azusalad.isosta.fake

import io.github.azusalad.isosta.data.IsostaThumbnailsRepository
import io.github.azusalad.isosta.model.Thumbnail

class FakeNetworkIsostaThumbnailsRepository :
    io.github.azusalad.isosta.data.IsostaThumbnailsRepository {
    override suspend fun getThumbnailPhotos(): List<io.github.azusalad.isosta.model.Thumbnail> {
        return FakeDataSource.thumbnailList
    }
}
