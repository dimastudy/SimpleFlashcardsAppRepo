package com.example.simpleflashcardsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleflashcardsapp.database.CardEntity
import com.example.simpleflashcardsapp.databinding.ItemWordBinding

class WordsListAdapter : ListAdapter<CardEntity, WordsListAdapter.WordsViewHolder>(DiffCallback) {


    class WordsViewHolder(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cardEntity: CardEntity) {
            binding.tvWordItem.text = cardEntity.word
            binding.tvTranslatedWordItem.text = cardEntity.translatedWord
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<CardEntity>() {
        override fun areItemsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder {
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
        val cardEntity = getItem(position)
        holder.bind(cardEntity)
    }

}