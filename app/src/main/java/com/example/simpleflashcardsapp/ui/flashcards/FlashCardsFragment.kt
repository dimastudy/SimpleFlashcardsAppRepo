package com.example.simpleflashcardsapp.ui.flashcards

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.adapters.CardsListAdapter
import com.example.simpleflashcardsapp.adapters.CardsListTouchHelper
import com.example.simpleflashcardsapp.databinding.FragmentFlashCardsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FlashCardsFragment : Fragment(R.layout.fragment_flash_cards) {

    private var _binding: FragmentFlashCardsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashcardsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFlashCardsBinding.bind(view)


        val adapter = CardsListAdapter(requireContext())
        val itemTouchHelper = CardsListTouchHelper(
            viewModel,
            adapter,
            requireContext(),
            binding.rvFlashCardsList.getRecyclerView()!!
        )

        val itemTouchHelperCallback = ItemTouchHelper(itemTouchHelper)
        binding.apply {
            btnLearnAgain.isVisible = false
            rvFlashCardsList.adapter = adapter
            itemTouchHelperCallback.attachToRecyclerView(rvFlashCardsList.getRecyclerView())
            btnLearnAgain.setOnClickListener {
                viewModel.resetAllWords()
                btnLearnAgain.isVisible = false
                adapter.notifyDataSetChanged()
            }

        }

        viewModel.cardsToLearn.observe(viewLifecycleOwner) { cards ->
            if (cards.size > 0) {
                adapter.submitList(cards)
            }
            else{
                binding.btnLearnAgain.isVisible = true
            }
        }

        viewModel.learnWord.observe(viewLifecycleOwner) { card ->
            if (card != null) {
                viewModel.updateCard(card.resetInstance())
            }
        }

        viewModel.wordLearned.observe(viewLifecycleOwner) { card ->
            if (card != null) {
                viewModel.updateCard(card.makeTrueLearned())
            }
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun ViewPager2.getRecyclerView(): RecyclerView? {
        try {
            val field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            field.isAccessible = true
            return field.get(this) as RecyclerView
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }
}