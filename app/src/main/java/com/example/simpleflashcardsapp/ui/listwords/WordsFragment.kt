package com.example.simpleflashcardsapp.ui.listwords

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.simpleflashcardsapp.R
import com.example.simpleflashcardsapp.adapters.WordsListAdapter
import com.example.simpleflashcardsapp.adapters.WordsListTouchHelper
import com.example.simpleflashcardsapp.databinding.FragmentWordsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordsFragment : Fragment(R.layout.fragment_words) {

    private var _binding: FragmentWordsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WordsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentWordsBinding.bind(view)

        val adapter = WordsListAdapter(WordsListAdapter.OnClickListener { cardDomain ->
            viewModel.navigateToAddingScreen(cardDomain)
        })

        val itemTouchHelper = WordsListTouchHelper(viewModel, adapter,requireContext())

        val itemTouchHelperCallback = ItemTouchHelper(itemTouchHelper)

        viewModel.navigateToAddScreen.observe(viewLifecycleOwner){  card ->
            if(card != null){
                val action = WordsFragmentDirections.actionWordsFragmentToAddingCardsFragment().setCard(card)
                view.findNavController().navigate(action)
            }
        }

        viewModel.words.observe(viewLifecycleOwner) { words ->
            adapter.submitList(words)
        }


        viewModel.cardDeleted.observe(viewLifecycleOwner) { card ->
            if (card != null) {
                viewModel.deleteCard(card)
                Snackbar.make(view, "Card deleted", Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo"
                    ) { viewModel.addCardToDatabase(card) }
                    .show()
                viewModel.doneDeleting()
            }
        }



        binding.apply {
            rvListWords.adapter = adapter
            rvListWords.setHasFixedSize(true)
            itemTouchHelperCallback.attachToRecyclerView(rvListWords)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}