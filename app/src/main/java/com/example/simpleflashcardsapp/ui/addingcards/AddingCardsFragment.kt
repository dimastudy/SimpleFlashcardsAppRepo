package com.example.simpleflashcardsapp.ui.addingcards

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.databinding.FragmentAddingCardBinding

class AddingCardsFragment: Fragment(R.layout.fragment_adding_card)  {

    private var _binding: FragmentAddingCardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddingCardBinding.bind(view)




        binding.btnAddWord.setOnClickListener {

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}