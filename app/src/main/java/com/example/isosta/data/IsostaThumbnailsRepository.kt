package com.example.isosta.data

import com.example.isosta.model.Thumbnail
//import com.example.network.IsostaApi
import com.example.network.IsostaApiService

interface IsostaThumbnailsRepository {
    suspend fun getThumbnailPhotos(): List<Thumbnail>
}

class NetworkIsostaThumbnailsRepository(
    // Dependency injection for what service to use
    private val isostaApiService: IsostaApiService
) : IsostaThumbnailsRepository {
    override suspend fun getThumbnailPhotos(): List<Thumbnail> {
        //return IsostaApi.IsostaService.getThumbnailPhotos()
        return isostaApiService.getThumbnailPhotos()
    }
}
