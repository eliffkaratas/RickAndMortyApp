package com.example.rickandmortyapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.base.BaseFragment
import com.example.rickandmortyapp.databinding.FragmentCharacterListBinding
import com.example.rickandmortyapp.network.Status
import com.example.rickandmortyapp.network.repository.EventObserver
import com.example.rickandmortyapp.util.hide
import com.example.rickandmortyapp.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterListFragment : BaseFragment<CharacterViewModel, FragmentCharacterListBinding>() {
    override var viewModel: CharacterViewModel? = null

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
                    it.data?.let { it1 ->
                        dataBinding.textView.text = it1.results.toString()
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
}