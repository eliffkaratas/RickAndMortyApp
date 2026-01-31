package com.example.rickandmortyapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.PagerItemBinding
import com.example.rickandmortyapp.model.CharacterResponse

class PagerAdapter(
    private val characterList: List<CharacterResponse.CharacterResult>,
    private val characterClicked: (CharacterResponse.CharacterResult) -> Unit
) : RecyclerView.Adapter<CharacterPagerViewHolder>() {

    override fun getItemCount(): Int {
        return if (characterList.isEmpty()) 0 else Int.MAX_VALUE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterPagerViewHolder {
        val itemBinding = PagerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterPagerViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CharacterPagerViewHolder, position: Int) {
        if (characterList.isEmpty()) return

        val realIndex = position % characterList.size
        val character = characterList[realIndex]

        holder.bind(character)
        holder.itemBinding.cardViewCharacter.setOnClickListener {
            characterClicked(character)
        }
    }

    fun startPosition(): Int {
        if (characterList.isEmpty()) return 0
        val middle = Int.MAX_VALUE / 2
        return middle - (middle % characterList.size)
    }
}

class CharacterPagerViewHolder(
    val itemBinding: PagerItemBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(character: CharacterResponse.CharacterResult) {
        itemBinding.character = character

        val id = character.id
        val colorRes = when {
            id != null && id % 3 == 0 -> R.color.holo_orange_light
            id != null && id % 2 == 0 -> R.color.win8_pink
            else -> null
        }

        colorRes?.let {
            itemBinding.textViewName.setTextColor(
                ContextCompat.getColor(itemView.context, it)
            )
        }

        Glide.with(itemView)
            .load(character.image)
            .apply(RequestOptions.circleCropTransform())
            .into(itemBinding.imageViewPager)

        itemBinding.executePendingBindings()
    }
}