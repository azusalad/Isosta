package com.example.isosta.fake

import com.example.isosta.model.Thumbnail
import com.example.network.IsostaApiService

class FakeIsostaApiService : IsostaApiService() {
    override suspend fun getThumbnailPhotos(url: String): List<Thumbnail> {
        return FakeDataSource.thumbnailList
    }
}
