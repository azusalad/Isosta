package com.example.isosta.fake

import com.example.isosta.data.IsostaThumbnailsRepository
import com.example.isosta.model.Thumbnail

class FakeNetworkIsostaThumbnailsRepository : IsostaThumbnailsRepository {
    override suspend fun getThumbnailPhotos(): List<Thumbnail> {
        return FakeDataSource.thumbnailList
    }
}
