package com.example.simpleflashcardsapp.ui.flashcards

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.adapters.CardsListAdapter
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
        binding.apply {
            rvFlashCardsList.adapter = adapter

        }

        viewModel.allCards.observe(viewLifecycleOwner){ cards ->
            adapter.submitList(cards)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}