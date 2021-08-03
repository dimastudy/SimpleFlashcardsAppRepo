package com.example.simpleflashcardsapp.ui.listwords

import androidx.lifecycle.*
import com.example.simpleflashcardsapp.data.CardsRepository
import com.example.simpleflashcardsapp.database.asDomainModel
import com.example.simpleflashcardsapp.domain.CardDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WordsViewModel @Inject constructor(
    private val repository: CardsRepository
) : ViewModel() {

    private val _cardDeleted = MutableLiveData<CardDomain?>()
    val cardDeleted: LiveData<CardDomain?>
        get() = _cardDeleted

    private val _cardAdded = MutableLiveData<Boolean>()
    val cardAdded: LiveData<Boolean>
        get() = _cardAdded

    private val _navigateToAddScreen = MutableLiveData<CardDomain?>()
    val navigateToAddScreen: LiveData<CardDomain?>
        get() = _navigateToAddScreen


    init {
        _navigateToAddScreen.value = null
        _cardDeleted.value = null
        _cardAdded.value = false
    }

    val words: LiveData<List<CardDomain>> =
        Transformations.map(repository.cardsDatabase) { cardsEntity ->
            cardsEntity.asDomainModel()
        }

    fun deleteCard(cardDomain: CardDomain) {
        viewModelScope.launch {
            repository.deleteFromDatabase(cardDomain)
        }
    }

    fun addCardToDatabase(cardDomain: CardDomain) {
        viewModelScope.launch {
            repository.insertWordToDatabase(cardDomain)
            _cardAdded.value = true
        }
    }

    fun navigateToAddingScreen(cardDomain: CardDomain) {
        _navigateToAddScreen.value = cardDomain
    }

    fun triggerDeleting(cardDomain: CardDomain){
        _cardDeleted.value = cardDomain
    }


    fun doneDeleting() {
        _cardDeleted.value = null
    }

    fun doneAdding() {
        _cardAdded.value = false
    }

    fun doneNavigatingToAddScreen() {
        _navigateToAddScreen.value = null
    }


}