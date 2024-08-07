package io.github.azusalad.isosta.data

import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.network.IsostaApiService

interface IsostaUserRepository {
    suspend fun getUserInfo(url: String): IsostaUser
    suspend fun getSearchInfo(query: String): List<IsostaUser>
}

class NetworkIsostaUserRepository(
    // Dependency injection for what service to use
    private val isostaApiService: IsostaApiService
) : IsostaUserRepository {
    override suspend fun getUserInfo(url: String): IsostaUser {
        return isostaApiService.getUserInfo(url)
    }
    override suspend fun getSearchInfo(query: String): List<IsostaUser> {
        return isostaApiService.getSearchInfo(query)
    }
}
