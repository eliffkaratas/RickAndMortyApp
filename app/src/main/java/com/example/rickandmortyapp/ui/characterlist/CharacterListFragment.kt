package com.example.rickandmortyapp.ui.characterlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rickandmortyapp.ui.model.CharacterArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterListFragment : Fragment() {

    private val viewModel: CharacterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    CharacterListRoute(
                        viewModel = viewModel,
                        onCharacterClick = { character ->
                            val args = CharacterArgs(
                                characterImage = character.image,
                                characterName = character.name,
                                characterStatus = character.status,
                                characterLocationName = character.characterLocation?.name,
                                characterLocationUrl = character.characterLocation?.url,
                                characterSpecies = character.species,
                                characterGender = character.gender,
                                characterCreated = character.created
                            )

                            findNavController().navigate(
                                CharacterListFragmentDirections
                                    .actionNavCharacterListToCharacterDetailFragment(args)
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadPage(1)
    }
}