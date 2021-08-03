package com.example.simpleflashcardsapp.ui.flashcards

import androidx.lifecycle.*
import com.example.simpleflashcardsapp.data.CardsRepository
import com.example.simpleflashcardsapp.database.asDomainModel
import com.example.simpleflashcardsapp.domain.CardDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FlashcardsViewModel @Inject constructor(
    private val repository: CardsRepository
) : ViewModel() {

    private val _learnWord = MutableLiveData<CardDomain?>()
    val learnWord: LiveData<CardDomain?>
        get() = _learnWord

    private val _wordLearned = MutableLiveData<CardDomain?>()
    val wordLearned: LiveData<CardDomain?>
        get() = _wordLearned


    init {
        _wordLearned.value = null
        _learnWord.value = null
    }

    fun updateCard(cardDomain: CardDomain) {
        viewModelScope.launch {
            repository.updateCard(cardDomain)
        }
    }

    fun resetAllWords() {
        viewModelScope.launch {
            val list = allCards.value
            if (list != null) {
                for (word in list) {
                    if (repository.isCardExist(word.id)) {
                        updateCard(word.resetInstance())
                    }
                }
            }
        }
    }


    val allCards: LiveData<List<CardDomain>> =
        Transformations.map(repository.cardsDatabase) { cards ->
            cards.asDomainModel()
        }

    val cardsToLearn = Transformations.map(allCards) { cards ->
        cards.filter { !it.isLearned }
    }

    fun toMakeWordLearned(cardDomain: CardDomain) {
        _wordLearned.value = cardDomain
    }

    fun wordLearnedMakingDone() {
        _wordLearned.value = null
    }

    fun toLearnWord(cardDomain: CardDomain) {
        _learnWord.value = cardDomain
    }

    fun doneLearning() {
        _learnWord.value = null
    }


}