package io.github.azusalad.isosta.data

import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.network.IsostaApiService

interface IsostaUserRepository {
    suspend fun getUserInfo(url: String): io.github.azusalad.isosta.model.IsostaUser
}

class NetworkIsostaUserRepository(
    // Dependency injection for what service to use
    private val isostaApiService: IsostaApiService
) : io.github.azusalad.isosta.data.IsostaUserRepository {
    override suspend fun getUserInfo(url: String): io.github.azusalad.isosta.model.IsostaUser {
        return isostaApiService.getUserInfo(url)
    }
}
