package io.github.azusalad.isosta.data

import io.github.azusalad.network.IsostaApiService
import android.content.Context

// The app container contains dependencies that the app requires.
interface AppContainer {
    val isostaPostRepository: IsostaPostRepository
    val isostaUserRepository: IsostaUserRepository

    val thumbnailRoomRepository: ThumbnailRoomRepository
    val userRoomRepository: UserRoomRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val isostaService : IsostaApiService by lazy {
        IsostaApiService()
    }

    override val isostaPostRepository: IsostaPostRepository by lazy {
        NetworkIsostaPostRepository(isostaService)
    }
    override val isostaUserRepository: IsostaUserRepository by lazy {
        NetworkIsostaUserRepository(isostaService)
    }

    override val thumbnailRoomRepository: ThumbnailRoomRepository by lazy {
        OfflineThumbnailRoomRepository(ThumbnailDatabase.loadDatabase(context).thumbnailDao())
    }
    override val userRoomRepository: UserRoomRepository by lazy {
        OfflineUserRoomRepository(UserDatabase.loadDatabase(context).userDao())
    }
}
