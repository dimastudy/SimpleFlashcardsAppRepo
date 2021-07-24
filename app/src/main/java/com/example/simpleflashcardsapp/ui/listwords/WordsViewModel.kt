package com.example.simpleflashcardsapp.ui.listwords

import androidx.lifecycle.ViewModel
import com.example.simpleflashcardsapp.data.CardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WordsViewModel @Inject constructor(
    private val repository: CardsRepository
) : ViewModel() {

    val words  = repository.cards


}