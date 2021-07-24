package com.example.simpleflashcardsapp.adapters


import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.database.CardEntity
import com.example.simpleflashcardsapp.databinding.FlashcardItemBinding

class CardsListAdapter(private val context: Context) :
    ListAdapter<CardEntity, CardsListAdapter.CardsViewHolder>(DiffCallback) {


    class CardsViewHolder(private val binding: FlashcardItemBinding, context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        val frontAnim = AnimatorInflater.loadAnimator(
            context.applicationContext,
            R.animator.front_animator
        ) as AnimatorSet
        val backAnim = AnimatorInflater.loadAnimator(
            context.applicationContext,
            R.animator.back_animator
        ) as AnimatorSet
        val scale = context.applicationContext.resources.displayMetrics.density
        private val SCALE_NUMBER = 8000 * scale

        init {
            binding.apply {
                cardWord.cameraDistance = SCALE_NUMBER
                cardTranslatedWord.cameraDistance = SCALE_NUMBER
            }
        }

        fun bind(cardEntity: CardEntity) {
            var isFront = true
            binding.apply {
                tvWord.text = cardEntity.word
                tvTranslatedWord.text = cardEntity.translatedWord
                cardItem.setOnClickListener {
                    if (isFront) {
                        frontAnim.setTarget(cardWord)
                        backAnim.setTarget(cardTranslatedWord)
                        frontAnim.start()
                        backAnim.start()
                        frontAnim.doOnEnd {
                            isFront = false
                        }

                    } else {
                        frontAnim.setTarget(cardTranslatedWord)
                        backAnim.setTarget(cardWord)
                        frontAnim.start()
                        backAnim.start()
                        frontAnim.doOnEnd {
                            isFront = true
                        }
                    }
                }
            }
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<CardEntity>() {
        override fun areItemsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean =
            oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        val binding =
            FlashcardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CardsViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }

    }


}