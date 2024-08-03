package com.example.isosta.data

import com.example.isosta.model.IsostaUser
import com.example.network.IsostaApiService

interface IsostaUserRepository {
    suspend fun getUserInfo(url: String): IsostaUser
}

class NetworkIsostaUserRepository(
    // Dependency injection for what service to use
    private val isostaApiService: IsostaApiService
) : IsostaUserRepository {
    override suspend fun getUserInfo(url: String): IsostaUser {
        return isostaApiService.getUserInfo(url)
    }
}
