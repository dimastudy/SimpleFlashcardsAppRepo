package com.example.simpleflashcardsapp.ui.addingcards

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleflashcardsapp.data.CardsRepository
import com.example.simpleflashcardsapp.database.CardEntity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddingViewModel @Inject constructor(
    private val repository: CardsRepository
) : ViewModel() {

    private val _translatedWord = MutableLiveData<String?>()
    val translatedWord: LiveData<String?>
        get() = _translatedWord

    private val _translationException = MutableLiveData<String?>()
    val translationException: LiveData<String?>
        get() = _translationException

    private val _isTranslatorDownloadedListener = MutableLiveData<String?>()
    val isTranslatorDownloadedListener: LiveData<String?>
        get() = _isTranslatorDownloadedListener


    private var isTranslatorDownloaded = false
    private lateinit var russianEnglishTranslator: Translator


    private val _isWordAdded = MutableLiveData<Boolean>()
    val isWordAdded: LiveData<Boolean>
        get() = _isWordAdded

    private val _navigateToFlashCards = MutableLiveData<Boolean>()
    val navigateToFlashCards: LiveData<Boolean>
        get() = _navigateToFlashCards

    init {
        downloadTranslator()
        _translatedWord.value = null
        _translationException.value = null
        _isTranslatorDownloadedListener.value = null
    }


    fun insertWord(word: String, translate: String) {
        val cardEntity = CardEntity(id = 0, word = word, translatedWord = translate)

        viewModelScope.launch {
            repository.insertWordToDatabase(cardEntity)
        }
    }

    private fun downloadTranslator() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.RUSSIAN)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        russianEnglishTranslator = Translation.getClient(options)
        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                russianEnglishTranslator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener {
                        isTranslatorDownloaded = true
                        _isTranslatorDownloadedListener.value = "Successfully downloaded Translator"
                    }
                    .addOnFailureListener { exception ->
                        isTranslatorDownloaded = false
                        _isTranslatorDownloadedListener.value = "Translator downloading failed"
                        Timber.e(exception)
                    }
            }
        }
    }

    fun translateWord(text: String){
        if(isTranslatorDownloaded){
            russianEnglishTranslator.translate(text)
                .addOnSuccessListener { word ->
                    _translatedWord.value = word
                }
                .addOnFailureListener {
                    _translationException.value = it.message.toString()
                }
        }
        else{
            _translationException.value = "Translator wasn't downloaded!"
        }
    }

    override fun onCleared() {
        super.onCleared()
        russianEnglishTranslator.close()
    }

    fun translatedWordDone(){
        _translatedWord.value = null
    }
    fun translatingExceptionDone(){
        _translationException.value = null
    }
    fun translatedListenerDone(){
        _isTranslatorDownloadedListener.value = null
    }


    fun addWord() {
        _isWordAdded.value = true
    }

    fun doneAdding() {
        _isWordAdded.value = false
    }

    fun navigateToFlashCardScreen() {
        _navigateToFlashCards.value = true
    }

    fun doneNavigatingToFlashCardScreen() {
        _navigateToFlashCards.value = false
    }


}