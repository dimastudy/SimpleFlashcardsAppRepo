package com.example.simpleflashcardsapp.ui.addingcards

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.databinding.FragmentAddingCardBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AddingCardsFragment : Fragment(R.layout.fragment_adding_card) {

    private var _binding: FragmentAddingCardBinding? = null
    private val binding get() = _binding!!
    private val TRANSLATOR_DOWNLOADED = "downloaded"
    private lateinit var sharedPref : SharedPreferences


    @Inject
    lateinit var viewModelFactory: AddingViewModel.AssistedFactory
    private val viewModel: AddingViewModel by viewModels {
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        var downloaded = false
        if (sharedPref.contains(TRANSLATOR_DOWNLOADED)){
            downloaded = sharedPref.getBoolean(TRANSLATOR_DOWNLOADED,false)
        }
        val cardDomain = AddingCardsFragmentArgs.fromBundle(requireArguments()).card
        AddingViewModel.provideFactory(viewModelFactory, cardDomain,downloaded)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddingCardBinding.bind(view)
        viewModel.isWordAdded.observe(viewLifecycleOwner) {
            if (it) {
                val wordText = binding.etWordToTranslate.text.toString()
                val translatedWord = binding.etTranslatedWord.text.toString()
                val cardDomain = AddingCardsFragmentArgs.fromBundle(requireArguments()).card
                if (wordText.isNotBlank() && translatedWord.isNotBlank()) {
                    if(cardDomain != null){
                        viewModel.insertWord(wordText, translatedWord, cardDomain.id)
                    }
                    else{
                        viewModel.insertWord(wordText, translatedWord)
                    }
                    view.findNavController().navigate(AddingCardsFragmentDirections.actionAddingCardsFragmentToWordsFragment())
                    viewModel.doneAdding()
                } else {
                    Snackbar.make(requireView(), "Fields can't be empty!", Snackbar.LENGTH_LONG)
                        .show()
                    viewModel.doneAdding()
                }
            }
        }
        

        viewModel.wordCardDomain.observe(viewLifecycleOwner){ card ->
            if(card != null){
                binding.apply {
                    etWordToTranslate.setText(card.word)
                    etTranslatedWord.setText(card.translatedWord)
                }
                viewModel.makeCardNull()
            }
        }


        viewModel.translatedWord.observe(viewLifecycleOwner) { translatedWord ->
            if (translatedWord != null) {
                binding.etTranslatedWord.setText(translatedWord)
                viewModel.translatedWordDone()
            }
        }


        viewModel.isTranslatorDownloadedListener.observe(viewLifecycleOwner) {
            if (it != null) {
                with(sharedPref.edit()){
                    putBoolean(TRANSLATOR_DOWNLOADED, true)
                    apply()
                }
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
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

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_adding_card,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_commit -> {
                viewModel.addWord()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}