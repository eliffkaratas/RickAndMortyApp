package com.example.rickandmortyapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.base.BaseFragment
import com.example.rickandmortyapp.databinding.FragmentCharacterListBinding
import com.example.rickandmortyapp.network.repository.CharacterUiEffect
import com.example.rickandmortyapp.network.repository.CharacterUiState
import com.example.rickandmortyapp.util.hide
import com.example.rickandmortyapp.util.show
import com.example.rickandmortyapp.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class CharacterListFragment : BaseFragment<FragmentCharacterListBinding>() {

    private val viewModel: CharacterViewModel by viewModels()

    private lateinit var pagerAdapter: PagerAdapter
    private var autoScrollJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadPage(1)
    }

    override fun onDestroyView() {
        stopAutoScroll()
        super.onDestroyView()
    }

    override fun observeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // STATE
                launch {
                    viewModel.uiState.collect { state ->
                        when (state) {
                            is CharacterUiState.Idle -> Unit

                            is CharacterUiState.Loading -> {
                                dataBinding.lottieLoading.show()
                                stopAutoScroll()
                            }

                            is CharacterUiState.Success -> {
                                dataBinding.lottieLoading.hide()
                                bindPager(state)
                                startAutoScroll(intervalMs = 3000L)
                            }

                            is CharacterUiState.Error -> {
                                dataBinding.lottieLoading.hide()
                                stopAutoScroll()
                            }
                        }
                    }
                }

                launch {
                    viewModel.uiEffect.collect { effect ->
                        when (effect) {
                            is CharacterUiEffect.ShowSnackbar -> {
                                dataBinding.root.showSnackbar(effect.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun bindPager(state: CharacterUiState.Success) {
        pagerAdapter = PagerAdapter(
            state.data.results
        ) { character ->
            characterClicked(
                character.image.orEmpty(),
                character.name.orEmpty(),
                character.status.orEmpty(),
                character.characterLocation?.name.orEmpty(),
                character.characterLocation?.url.orEmpty(),
                character.species.orEmpty(),
                character.gender.orEmpty(),
                character.created.orEmpty()
            )
        }

        dataBinding.apply {
            textViewInfo.text = getString(R.string.info)
            viewPagerCharacters.adapter = pagerAdapter
            viewPagerCharacters.setCurrentItem(pagerAdapter.startPosition(), false)
            viewPagerCharacters.clipToPadding = false
            viewPagerCharacters.clipChildren = false
            viewPagerCharacters.offscreenPageLimit = 3
            viewPagerCharacters.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        setPageTransformer()
    }

    private fun setPageTransformer() {
        val transformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }
        }
        dataBinding.viewPagerCharacters.setPageTransformer(transformer)
    }

    private fun startAutoScroll(intervalMs: Long) {
        stopAutoScroll()

        autoScrollJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                delay(intervalMs)

                if (!this@CharacterListFragment::pagerAdapter.isInitialized) continue

                dataBinding.viewPagerCharacters.currentItem += 1
            }
        }
    }

    private fun stopAutoScroll() {
        autoScrollJob?.cancel()
        autoScrollJob = null
    }

    override fun getLayoutID(): Int = R.layout.fragment_character_list

    private fun characterClicked(
        characterImage: String,
        characterName: String,
        characterStatus: String,
        characterLocationName: String,
        characterLocationUrl: String,
        characterSpecies: String,
        characterGender: String,
        characterCreated: String
    ) {
        findNavController().navigate(
            CharacterListFragmentDirections.actionNavCharacterListToCharacterDetailFragment(
                characterImage,
                characterName,
                characterStatus,
                characterLocationName,
                characterLocationUrl,
                characterSpecies,
                characterGender,
                characterCreated
            )
        )
    }
}