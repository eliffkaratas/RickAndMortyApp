package com.example.rickandmortyapp.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.base.BaseFullBottomSheetFragment
import com.example.rickandmortyapp.databinding.FragmentCharacterDetailBinding

class CharacterDetailFragment :
    BaseFullBottomSheetFragment<Nothing, FragmentCharacterDetailBinding>() {

    override var viewModel: Nothing? = null

    private val args: CharacterDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.close -> {
                    dismiss()
                    true
                }
                else -> false
            }
        }

        bindUi()
    }

    private fun bindUi() = with(dataBinding) {
        Glide.with(this@CharacterDetailFragment)
            .load(args.characterImage)
            .into(imageViewCharacterDetail)

        textViewName.text = args.characterName
        textViewStatus.text = args.characterStatus
        textViewLocationName.text = args.characterLocationName
        textViewSpecies.text = args.characterSpecies
        textViewGender.text = args.characterGender
        textViewCreated.text = args.characterCreated
        args.characterLocationUrl?.let {
            textViewLocationUrl.text = it
        } ?: run {
            textViewLocationUrl.visibility = View.GONE
        }
    }

    override fun getLayoutID(): Int = R.layout.fragment_character_detail
    override fun observeLiveData() {}
}