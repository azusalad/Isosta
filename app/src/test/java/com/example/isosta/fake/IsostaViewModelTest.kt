package com.example.isosta.fake

import com.example.isosta.rules.TestDispatcherRule
import com.example.isosta.ui.screens.IsostaUiState
import com.example.isosta.ui.screens.IsostaViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

// This test does not work
class IsostaViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun isostaViewModel_getThumbnailPhotos_verifyIsostaUiStateSuccess() =
        runTest {
            val isostaViewModel = IsostaViewModel(
                isostaThumbnailsRepository = FakeNetworkIsostaThumbnailsRepository()
            )
            assertEquals(
                IsostaUiState.Success("Success: ${FakeDataSource.thumbnailList.size} thumbnails received"), isostaViewModel.isostaUiState
            )
        }
}
