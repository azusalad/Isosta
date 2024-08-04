package io.github.azusalad.isosta.data

import io.github.azusalad.isosta.model.IsostaPost
import io.github.azusalad.isosta.model.Thumbnail
import io.github.azusalad.network.IsostaApiService

interface IsostaPostRepository {
    suspend fun getPostInfo(url: String): io.github.azusalad.isosta.model.IsostaPost
}

class NetworkIsostaPostRepository(
    // Dependency injection for what service to use
    private val isostaApiService: IsostaApiService
) : io.github.azusalad.isosta.data.IsostaPostRepository {
    override suspend fun getPostInfo(url: String): io.github.azusalad.isosta.model.IsostaPost {
        return isostaApiService.getPostInfo(url)
    }
}
