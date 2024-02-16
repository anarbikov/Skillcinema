package com.skillcinema.ui.profile


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.skillcinema.domain.CleanCollectionUseCase
import com.skillcinema.domain.DeleteCollectionUseCase
import com.skillcinema.domain.GetFullCollectionsUseCase
import com.skillcinema.domain.InsertCollectionUseCase
import com.skillcinema.room.CollectionWIthFilms
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.mock

class ProfileViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatcherRule: TestRule = InstantTaskExecutorRule()
    private lateinit var getFullCollectionsUseCaseMock: GetFullCollectionsUseCase
    private lateinit var cleanCollectionUseCaseMock: CleanCollectionUseCase
    private lateinit var insertCollectionUseCaseMock: InsertCollectionUseCase
    private lateinit var deleteCollectionUseCaseMock: DeleteCollectionUseCase
    private lateinit var viewModel: ProfileViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() = runTest {
        Dispatchers.setMain(testDispatcher)
        getFullCollectionsUseCaseMock = mock<GetFullCollectionsUseCase>()
        cleanCollectionUseCaseMock = mock<CleanCollectionUseCase>()
        insertCollectionUseCaseMock = mock<InsertCollectionUseCase>()
        deleteCollectionUseCaseMock = mock<DeleteCollectionUseCase>()
        viewModel = ProfileViewModel(
            getFullCollectionsUseCaseMock,
            cleanCollectionUseCaseMock,
            insertCollectionUseCaseMock,
            deleteCollectionUseCaseMock
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun resetData() {
        Dispatchers.resetMain()
        Mockito.reset(getFullCollectionsUseCaseMock)
        Mockito.reset(cleanCollectionUseCaseMock)
        Mockito.reset(insertCollectionUseCaseMock)
        Mockito.reset(deleteCollectionUseCaseMock)
    }

    @Test
    fun `function load all should return data class by state`() = runTest {
        val collectionMock = mock<List<CollectionWIthFilms>>()
        Mockito.`when`(getFullCollectionsUseCaseMock.execute()).thenReturn(collectionMock)
        viewModel.getCollectionsList()
        val actual = viewModel.collections.value
        Assertions.assertEquals(collectionMock, actual)
    }

}