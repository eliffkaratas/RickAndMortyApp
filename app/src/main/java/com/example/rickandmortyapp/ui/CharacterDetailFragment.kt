package com.example.rickandmortyapp.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.base.BaseFullBottomSheetFragment
import com.example.rickandmortyapp.databinding.FragmentCharacterDetailBinding

class CharacterDetailFragment :
    BaseFullBottomSheetFragment<CharacterViewModel, FragmentCharacterDetailBinding>() {
    override var viewModel: CharacterViewModel? = null
    private val args: CharacterDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.apply {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.close -> {
                        dismiss()
                        true
                    }
                    else -> false
                }
            }
        }
        dataBinding.textView.text = args.characterId
    }

    override fun getLayoutID(): Int = R.layout.fragment_character_detail
    override fun observeLiveData() {}
}