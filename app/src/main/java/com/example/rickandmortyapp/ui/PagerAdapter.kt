package com.example.rickandmortyapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.PagerItemBinding
import com.example.rickandmortyapp.model.CharacterResponse

class PagerAdapter(
    private val viewPager: ViewPager2,
    private val context: Context,
    private val characterList: MutableList<CharacterResponse.CharacterResult>,
    private val characterClicked: (character: CharacterResponse.CharacterResult) -> Unit
) : RecyclerView.Adapter<CharacterPagerViewHolder>() {

    override fun getItemCount(): Int = characterList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CharacterPagerViewHolder {
        val itemBinding =
            PagerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        return CharacterPagerViewHolder(itemBinding, context)
    }

    override fun onBindViewHolder(holder: CharacterPagerViewHolder, position: Int) {
        val character = characterList[position]
        if (position == characterList.size - 2) {
            viewPager.post(run)
        }
        holder.bind(character)
        holder.itemBinding.cardViewCharacter.setOnClickListener {
            characterClicked.invoke(character)
        }
    }

    private val run = Runnable {
        characterList.addAll(characterList)
        notifyDataSetChanged()
    }
}

class CharacterPagerViewHolder(
    val itemBinding: PagerItemBinding,
    val context: Context
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(character: CharacterResponse.CharacterResult) {
        itemBinding.character = character
        if (character.id != null) {
            if (character.id % 2 == 0) {
                itemBinding.textViewName.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.win8_pink
                    )
                )
            }
        }
        if (character.id != null) {
            if (character.id % 3 == 0) {
                itemBinding.textViewName.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.holo_orange_light
                    )
                )
            }
        }
        Glide.with(itemView.context).load(character.image).apply(
            RequestOptions.circleCropTransform()
        ).into(itemBinding.imageViewPager)
        itemBinding.executePendingBindings()
    }
}