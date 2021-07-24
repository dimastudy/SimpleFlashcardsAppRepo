package com.example.simpleflashcardsapp.ui.flashcards

import androidx.lifecycle.ViewModel
import com.example.simpleflashcardsapp.data.CardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FlashcardsViewModel @Inject constructor(
    private val repository: CardsRepository
) : ViewModel() {

    val allCards = repository.cards



}