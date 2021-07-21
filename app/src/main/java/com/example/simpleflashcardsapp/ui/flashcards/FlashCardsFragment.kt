package com.example.simpleflashcardsapp.ui.flashcards

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.databinding.FragmentFlashCardsBinding

class FlashCardsFragment : Fragment(R.layout.fragment_flash_cards) {

    private var _binding: FragmentFlashCardsBinding? = null
    private val binding get() = _binding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFlashCardsBinding.bind(view)



    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}