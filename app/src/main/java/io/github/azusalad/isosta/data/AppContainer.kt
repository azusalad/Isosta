package io.github.azusalad.isosta.data

import io.github.azusalad.network.IsostaApiService

// The app container contains dependencies that the app requires.
interface AppContainer {
    val isostaThumbnailsRepository: io.github.azusalad.isosta.data.IsostaThumbnailsRepository
    val isostaPostRepository: io.github.azusalad.isosta.data.IsostaPostRepository
    val isostaUserRepository: io.github.azusalad.isosta.data.IsostaUserRepository
}

class DefaultAppContainer : io.github.azusalad.isosta.data.AppContainer {

    private val isostaService : IsostaApiService by lazy {
        IsostaApiService()
    }

    override val isostaThumbnailsRepository: io.github.azusalad.isosta.data.IsostaThumbnailsRepository by lazy {
        io.github.azusalad.isosta.data.NetworkIsostaThumbnailsRepository(isostaService)
    }
    override val isostaPostRepository: io.github.azusalad.isosta.data.IsostaPostRepository by lazy {
        io.github.azusalad.isosta.data.NetworkIsostaPostRepository(isostaService)
    }
    override val isostaUserRepository: io.github.azusalad.isosta.data.IsostaUserRepository by lazy {
        io.github.azusalad.isosta.data.NetworkIsostaUserRepository(isostaService)
    }
}
