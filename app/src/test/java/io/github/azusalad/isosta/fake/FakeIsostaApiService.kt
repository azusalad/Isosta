package io.github.azusalad.isosta.fake

import io.github.azusalad.isosta.model.Thumbnail
import io.github.azusalad.network.IsostaApiService

class FakeIsostaApiService : IsostaApiService() {
    override suspend fun getThumbnailPhotos(url: String): List<io.github.azusalad.isosta.model.Thumbnail> {
        return FakeDataSource.thumbnailList
    }
}
