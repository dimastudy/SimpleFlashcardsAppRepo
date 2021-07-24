package com.example.simpleflashcardsapp.ui.addingcards

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.databinding.FragmentAddingCardBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddingCardsFragment : Fragment(R.layout.fragment_adding_card) {

    private var _binding: FragmentAddingCardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddingCardBinding.bind(view)
        binding.pbWordTranslated.isVisible = false
        viewModel.isWordAdded.observe(viewLifecycleOwner) {
            if (it) {
                val wordText = binding.etWordToTranslate.text.toString()
                val translatedWord = binding.etTranslatedWord.text.toString()

                if (wordText.isNotBlank() && translatedWord.isNotBlank()) {
                    viewModel.insertWord(wordText, translatedWord)
                } else {
                    Snackbar.make(requireView(), "Fields can't be empty!", Snackbar.LENGTH_LONG)
                        .show()
                    viewModel.doneAdding()
                }
            }
        }

        viewModel.translatedWord.observe(viewLifecycleOwner) { translatedWord ->
            if (translatedWord != null) {
                binding.etTranslatedWord.setText(translatedWord)
                viewModel.translatedWordDone()
            }
        }



        viewModel.isTranslatorDownloadedListener.observe(viewLifecycleOwner) {
            if (it != null){
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                viewModel.translatedListenerDone()
            }
        }

        viewModel.translationException.observe(viewLifecycleOwner) {
            if (it != null) {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                viewModel.translatingExceptionDone()
            }
        }


        binding.btnTranslateWord.setOnClickListener {
            val text = binding.etWordToTranslate.text.toString()
            viewModel.translateWord(text)
        }


        /*viewModel.navigateToFlashCards.observe(viewLifecycleOwner) {
            if (it) {
                view.findNavController().navigate(
                    AddingCardsFragmentDirections
                        .actionAddingCardsFragmentToFlashCardsFragment()
                )

                viewModel.doneNavigatingToFlashCardScreen()
            }
        }*/

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}