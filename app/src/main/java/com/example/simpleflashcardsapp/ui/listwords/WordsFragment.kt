package com.example.simpleflashcardsapp.ui.listwords

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.adapters.WordsListAdapter
import com.example.simpleflashcardsapp.databinding.FragmentWordsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordsFragment: Fragment(R.layout.fragment_words) {

    private var _binding: FragmentWordsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WordsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentWordsBinding.bind(view)

        val adapter = WordsListAdapter()

        viewModel.words.observe(viewLifecycleOwner){ words ->
            adapter.submitList(words)
        }

        binding.apply {
            rvListWords.adapter = adapter
            rvListWords.setHasFixedSize(true)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}