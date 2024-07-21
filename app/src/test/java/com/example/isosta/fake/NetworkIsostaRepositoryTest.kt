package com.example.isosta.fake

import com.example.isosta.data.NetworkIsostaThumbnailsRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlinx.coroutines.test.runTest

class NetworkIsostaRepositoryTest {
    @Test
    fun networkIsostaThumbnailsRepository_getThumbnailPhotos_verifyPhotoList() =
        // Need to call a suspend function from without a coroutine
        runTest {
            val repository = NetworkIsostaThumbnailsRepository(
                isostaApiService = FakeIsostaApiService()
            )
            assertEquals(FakeDataSource.thumbnailList, repository.getThumbnailPhotos())
        }
}
