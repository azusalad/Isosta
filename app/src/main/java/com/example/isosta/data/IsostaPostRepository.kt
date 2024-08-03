package com.example.isosta.data

import com.example.isosta.model.IsostaPost
import com.example.isosta.model.Thumbnail
import com.example.network.IsostaApiService

interface IsostaPostRepository {
    suspend fun getPostInfo(url: String): IsostaPost
}

class NetworkIsostaPostRepository(
    // Dependency injection for what service to use
    private val isostaApiService: IsostaApiService
) : IsostaPostRepository {
    override suspend fun getPostInfo(url: String): IsostaPost {
        return isostaApiService.getPostInfo(url)
    }
}