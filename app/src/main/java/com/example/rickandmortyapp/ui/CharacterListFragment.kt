package com.example.rickandmortyapp.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.base.BaseFragment
import com.example.rickandmortyapp.databinding.FragmentCharacterListBinding
import com.example.rickandmortyapp.network.Status
import com.example.rickandmortyapp.network.repository.EventObserver
import com.example.rickandmortyapp.util.hide
import com.example.rickandmortyapp.util.show
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.abs

@AndroidEntryPoint
class CharacterListFragment : BaseFragment<CharacterViewModel, FragmentCharacterListBinding>() {
    override var viewModel: CharacterViewModel? = null

    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var pagerHandler: Handler
    private lateinit var pagerRun: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[CharacterViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.getCharacters()
    }

    override fun observeLiveData() {
        viewModel?.characterResponse(1)?.observe(viewLifecycleOwner, EventObserver {
            when (it.status) {
                Status.LOADING -> {
                    dataBinding.lottieLoading.show()
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        pagerAdapter = PagerAdapter(
                            dataBinding.viewPagerCharacters,
                            requireContext(),
                            it.results
                        ) { character ->
                            characterClicked(
                                character.image!!,
                                character.name!!,
                                character.status!!,
                                character.characterLocation?.name!!,
                                character.characterLocation.url!!,
                                character.species!!,
                                character.gender!!,
                                character.created!!
                            )
                        }
                        dataBinding.apply {
                            textViewInfo.text = getString(R.string.info)
                            viewPagerCharacters.adapter =
                                pagerAdapter
                            viewPagerCharacters.clipToPadding = false
                            viewPagerCharacters.clipChildren = false
                            viewPagerCharacters.offscreenPageLimit = 3
                            viewPagerCharacters.getChildAt(0).overScrollMode =
                                RecyclerView.OVER_SCROLL_NEVER
                        }
                        comPosPage()
                    }
                    dataBinding.lottieLoading.hide()
                }
                Status.ERROR -> {
                    dataBinding.lottieLoading.hide()
                }
            }
        })
    }

    override fun getLayoutID(): Int = R.layout.fragment_character_list

    fun comPosPage() {
        val comPosPageTarn = CompositePageTransformer()
        comPosPageTarn.addTransformer(MarginPageTransformer(40))
        comPosPageTarn.addTransformer { page, position ->
            val r: Float = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        dataBinding.viewPagerCharacters.setPageTransformer(comPosPageTarn)
        pagerHandler = Handler()
        pagerRun = Runnable {
            dataBinding.viewPagerCharacters.currentItem =
                dataBinding.viewPagerCharacters.currentItem + 1
        }
    }

    private fun characterClicked(
        characterImage: String,
        characterName: String,
        characterStatus: String,
        characterLocationName: String,
        characterLocationUrl: String,
        characterSpecies: String,
        characterGender: String,
        characterCreated: String
    ) =
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