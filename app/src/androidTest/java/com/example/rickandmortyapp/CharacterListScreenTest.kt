package com.example.rickandmortyapp

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rickandmortyapp.model.CharacterResponse
import com.example.rickandmortyapp.network.repository.CharacterUiState
import com.example.rickandmortyapp.ui.characterlist.CharacterListScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterListScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<TestComposeActivity>()

    @Test
    fun characterList_success_state_displays_character_name() {
        composeRule.setContent {
            CharacterListScreen(
                uiState = fakeSuccessState(),
                snackbarHostState = SnackbarHostState(),
                onRetry = {},
                onCharacterClick = {}
            )
        }

        composeRule.waitForIdle()

        assertAnyNodeWithTextIsDisplayed("Alien Morty")
    }

    private fun assertAnyNodeWithTextIsDisplayed(text: String) {
        val nodes = composeRule.onAllNodesWithText(text)

        val count = nodes.fetchSemanticsNodes().size

        val anyDisplayed = (0 until count).any { index ->
            nodes[index].isDisplayed()
        }

        assertTrue("Expected at least one displayed node containing '$text' but none was displayed.", anyDisplayed)
    }
}

private fun fakeSuccessState(): CharacterUiState.Success {
    return CharacterUiState.Success(
        data = CharacterResponse(
            info = CharacterResponse.CharacterInfo(
                count = 1,
                pages = 1,
                next = null,
                prev = null
            ),
            results = listOf(
                CharacterResponse.CharacterResult(
                    id = 1,
                    name = "Alien Morty",
                    status = "unknown",
                    species = "Alien",
                    gender = "Male",
                    image = "",
                    created = "",
                    characterLocation = CharacterResponse.CharacterResult.CharacterLocation(
                        name = "Unknown",
                        url = ""
                    ),
                    characterOrigin = CharacterResponse.CharacterResult.CharacterOrigin(
                        name = "Unknown",
                        url = ""
                    ),
                    type = "",
                    episode = emptyList(),
                    url = ""
                )
            )
        )
    )
}