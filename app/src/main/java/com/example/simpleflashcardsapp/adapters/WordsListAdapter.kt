package com.example.simpleflashcardsapp.adapters

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.databinding.ItemWordBinding
import com.example.simpleflashcardsapp.domain.CardDomain
import com.example.simpleflashcardsapp.ui.listwords.WordsViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class WordsListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<CardDomain, WordsListAdapter.WordsViewHolder>(DiffCallback) {


    class WordsViewHolder(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cardEntity: CardDomain) {
            binding.tvWordItem.text = cardEntity.word
            binding.tvTranslatedWordItem.text = cardEntity.translatedWord
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<CardDomain>() {
        override fun areItemsTheSame(oldItem: CardDomain, newItem: CardDomain): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CardDomain, newItem: CardDomain): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder {
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
        val cardDomain = getItem(position)
        holder.bind(cardDomain)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(cardDomain)
        }
    }

    fun getWordByPosition(position: Int): CardDomain =
        getItem(position)


    class OnClickListener(val clickListener: (cardDomain: CardDomain) -> Unit) {
        fun onClick(cardDomain: CardDomain) = clickListener(cardDomain)
    }


}

/*
val itemCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val item = viewHolder.adapterPosition


        when(direction){
            ItemTouchHelper.LEFT -> {

            }
        }
    }

}*/


class WordsListTouchHelper(val viewModel: WordsViewModel, private val adapter: WordsListAdapter,private val context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean =
        false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val viewHolderPosition = viewHolder.adapterPosition
        val card = adapter.getWordByPosition(viewHolderPosition)

        when (direction) {
            ItemTouchHelper.LEFT -> {
                viewModel.triggerDeleting(card)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(context,R.color.deleting_background))
            .addSwipeLeftActionIcon(R.drawable.ic_trash_delete_card)
            .addSwipeLeftLabel("Delete")
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


}