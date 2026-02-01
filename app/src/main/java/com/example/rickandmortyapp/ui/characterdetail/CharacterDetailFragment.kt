package com.example.rickandmortyapp.ui.characterdetail

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
            .load(args.detailArgs.characterImage)
            .into(imageViewCharacterDetail)

        textViewName.text = args.detailArgs.characterName
        textViewStatus.text = args.detailArgs.characterStatus
        textViewLocationName.text = args.detailArgs.characterLocationName
        textViewSpecies.text = args.detailArgs.characterSpecies
        textViewGender.text = args.detailArgs.characterGender
        textViewCreated.text = args.detailArgs.characterCreated
        args.detailArgs.characterLocationUrl?.let {
            textViewLocationUrl.text = it
        } ?: run {
            textViewLocationUrl.visibility = View.GONE
        }
    }

    override fun getLayoutID(): Int = R.layout.fragment_character_detail
    override fun observeLiveData() {}
}