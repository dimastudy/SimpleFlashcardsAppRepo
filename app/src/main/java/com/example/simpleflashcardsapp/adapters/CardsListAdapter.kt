package com.example.simpleflashcardsapp.adapters


import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.databinding.FlashcardItemBinding
import com.example.simpleflashcardsapp.domain.CardDomain
import com.example.simpleflashcardsapp.ui.flashcards.FlashcardsViewModel
import com.example.simpleflashcardsapp.ui.listwords.WordsViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class CardsListAdapter(private val context: Context) :
    ListAdapter<CardDomain, CardsListAdapter.CardsViewHolder>(DiffCallback) {


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

        fun bind(cardEntity: CardDomain) {
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


    object DiffCallback : DiffUtil.ItemCallback<CardDomain>() {
        override fun areItemsTheSame(oldItem: CardDomain, newItem: CardDomain): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CardDomain, newItem: CardDomain): Boolean =
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


    fun getWordByPosition(position: Int): CardDomain =
        getItem(position)
}

class CardsListTouchHelper(
    private val viewModel: FlashcardsViewModel,
    private val adapter: CardsListAdapter,
    private val context: Context,
    private val recyclerView: RecyclerView
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean =
        false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val viewHolderPosition = viewHolder.adapterPosition
        val card = adapter.getWordByPosition(viewHolderPosition)
        val lastPosition = adapter.itemCount - 1
        when (direction) {
            ItemTouchHelper.LEFT -> {
                viewModel.toMakeWordLearned(card)
                if (viewHolderPosition != lastPosition) {
                    recyclerView.scrollToPosition(viewHolderPosition + 1)
                }
            }
            ItemTouchHelper.RIGHT -> {
                viewModel.toLearnWord(card)
                if (viewHolderPosition != lastPosition) {
                    recyclerView.scrollToPosition(viewHolderPosition + 1)
                }
                if (viewHolderPosition == lastPosition && adapter.itemCount != 0) {
                    recyclerView.scrollToPosition(0)
                }
                if (adapter.itemCount == 1) {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    /*override fun onChildDraw(
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
            .addSwipeLeftBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.learned_background
                )
            )
            .addSwipeRightBackgroundColor(ContextCompat.getColor(
                context,
                R.color.deleting_background
            ))
            .addSwipeRightLabel("Repeat")
            .addSwipeLeftLabel("Learned")
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
*/
}