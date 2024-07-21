package com.example.isosta.data

import com.example.network.IsostaApiService

// The app container contains dependencies that the app requires.
interface AppContainer {
    val isostaThumbnailsRepository: IsostaThumbnailsRepository
}

class DefaultAppContainer : AppContainer {

    private val isostaService : IsostaApiService by lazy {
        IsostaApiService()
    }

    override val isostaThumbnailsRepository: IsostaThumbnailsRepository by lazy {
        NetworkIsostaThumbnailsRepository(isostaService)
    }
}
