package io.github.azusalad.isosta.data

import io.github.azusalad.isosta.model.Thumbnail
//import io.github.azusalad.network.IsostaApi
import io.github.azusalad.network.IsostaApiService

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
