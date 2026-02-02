package com.example.rickandmortyapp

import app.cash.turbine.test
import app.cash.turbine.testIn
import com.example.rickandmortyapp.model.CharacterResponse
import com.example.rickandmortyapp.network.Resource
import com.example.rickandmortyapp.network.Status
import com.example.rickandmortyapp.network.repository.CharacterRepository
import com.example.rickandmortyapp.network.repository.CharacterUiEffect
import com.example.rickandmortyapp.network.repository.CharacterUiState
import com.example.rickandmortyapp.ui.characterlist.CharacterViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.test.runCurrent
import app.cash.turbine.turbineScope

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repo: CharacterRepository = mockk()

    @Test
    fun `loadPage emits Loading then Success when repo returns success`() = runTest {
        val vm = CharacterViewModel(repo)

        val fakeResponse = CharacterResponse(
            info = CharacterResponse.CharacterInfo(count = 1, pages = 1, next = null, prev = null),
            results = emptyList()
        )

        coEvery { repo.getCharacters(1) } returns Resource.Companion.success(fakeResponse)

        vm.uiState.test {
            // initial
            assertEquals(CharacterUiState.Idle, awaitItem())

            vm.loadPage(1)

            // Loading
            assertEquals(CharacterUiState.Loading, awaitItem())

            // run queued coroutines
            advanceUntilIdle()

            // Success
            val success = awaitItem() as CharacterUiState.Success
            assertEquals(fakeResponse, success.data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadPage emits Error and snackbar effect when repo returns error`() = runTest {
        val vm = CharacterViewModel(repo)
        coEvery { repo.getCharacters(1) } returns Resource.error("boom")

        turbineScope {
            val stateTurbine = vm.uiState.testIn(backgroundScope)
            val effectTurbine = vm.uiEffect.testIn(backgroundScope)

            assertEquals(CharacterUiState.Idle, stateTurbine.awaitItem())

            vm.loadPage(1)
            assertEquals(CharacterUiState.Loading, stateTurbine.awaitItem())

            advanceUntilIdle()
            runCurrent()

            val err = stateTurbine.awaitItem() as CharacterUiState.Error
            assertEquals("boom", err.message)

            val effect = effectTurbine.awaitItem()
            assertEquals(CharacterUiEffect.ShowSnackbar("boom"), effect)

            stateTurbine.cancel()
            effectTurbine.cancel()
        }
    }

    @Test
    fun `loadPage emits Empty body error and snackbar when success data is null`() = runTest {
        val vm = CharacterViewModel(repo)
        coEvery { repo.getCharacters(1) } returns Resource(Status.SUCCESS, null, null, null)

        turbineScope {
            val stateTurbine = vm.uiState.testIn(backgroundScope)
            val effectTurbine = vm.uiEffect.testIn(backgroundScope)

            assertEquals(CharacterUiState.Idle, stateTurbine.awaitItem())

            vm.loadPage(1)
            assertEquals(CharacterUiState.Loading, stateTurbine.awaitItem())

            advanceUntilIdle()
            runCurrent()

            val err = stateTurbine.awaitItem() as CharacterUiState.Error
            assertEquals("Empty body", err.message)

            val effect = effectTurbine.awaitItem()
            assertEquals(CharacterUiEffect.ShowSnackbar("Empty body"), effect)

            stateTurbine.cancel()
            effectTurbine.cancel()
        }
    }

    @Test
    fun `refresh uses last page`() = runTest {
        val vm = CharacterViewModel(repo)

        val fakeResponse = CharacterResponse(
            info = CharacterResponse.CharacterInfo(count = 1, pages = 1, next = null, prev = null),
            results = emptyList()
        )

        coEvery { repo.getCharacters(3) } returns Resource.success(fakeResponse)

        vm.uiState.test {
            assertEquals(CharacterUiState.Idle, awaitItem())

            vm.loadPage(3)
            assertEquals(CharacterUiState.Loading, awaitItem())
            advanceUntilIdle()
            awaitItem() // Success

            vm.refresh() // tekrar 3
            assertEquals(CharacterUiState.Loading, awaitItem())
            advanceUntilIdle()
            awaitItem() // Success

            cancelAndIgnoreRemainingEvents()
        }
    }
}