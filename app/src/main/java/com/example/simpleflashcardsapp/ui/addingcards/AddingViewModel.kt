package com.example.simpleflashcardsapp.ui.addingcards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleflashcardsapp.data.CardsRepository
import com.example.simpleflashcardsapp.database.CardEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddingViewModel @Inject constructor(
    private val repository: CardsRepository
) : ViewModel() {

    private val _isWordAdded = MutableLiveData<Boolean>()
    val isWordAdded: LiveData<Boolean>
        get() = _isWordAdded

    fun insertWord(word: String, translate: String){
        val cardEntity = CardEntity(id = 0,word = word, translatedWord = translate)

        viewModelScope.launch {
            repository.insertWordToDatabase(cardEntity)
        }
    }

    fun addWord(){
        _isWordAdded.value = true
    }

    fun wordAdded(){
        _isWordAdded.value = false
    }


}