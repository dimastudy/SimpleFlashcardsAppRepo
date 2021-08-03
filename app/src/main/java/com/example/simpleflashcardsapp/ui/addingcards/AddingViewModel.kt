package com.example.simpleflashcardsapp.ui.addingcards

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.simpleflashcardsapp.data.CardsRepository
import com.example.simpleflashcardsapp.domain.CardDomain
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AddingViewModel @AssistedInject constructor(
    @Assisted private val cardDomain: CardDomain?,
    @Assisted private var isTranslatorDownloaded: Boolean,
    private val repository: CardsRepository
) : ViewModel() {

    private val _wordCardDomain = MutableLiveData<CardDomain?>()
    val wordCardDomain: LiveData<CardDomain?>
        get() = _wordCardDomain

    private val _translatedWord = MutableLiveData<String?>()
    val translatedWord: LiveData<String?>
        get() = _translatedWord

    private val _translationException = MutableLiveData<String?>()
    val translationException: LiveData<String?>
        get() = _translationException

    private val _isTranslatorDownloadedListener = MutableLiveData<String?>()
    val isTranslatorDownloadedListener: LiveData<String?>
        get() = _isTranslatorDownloadedListener


    private var russianEnglishTranslator: Translator? = null


    private val _isWordAdded = MutableLiveData<Boolean>()
    val isWordAdded: LiveData<Boolean>
        get() = _isWordAdded

    private val _navigateToFlashCards = MutableLiveData<Boolean>()
    val navigateToFlashCards: LiveData<Boolean>
        get() = _navigateToFlashCards

    init {
        downloadTranslator()
        _wordCardDomain.value = cardDomain
        _translatedWord.value = null
        _translationException.value = null
        _isTranslatorDownloadedListener.value = null

    }


    fun isCardExist(id: Int): Boolean {
        var exist = false
        viewModelScope.launch { exist = repository.isCardExist(id) }
        return exist
    }


    fun insertWord(word: String, translate: String, id: Int = 0) {
        if (id != 0) {
            if (isCardExist(id)) {
                val cardDomain = CardDomain(word = word, translatedWord = translate, id = id)
                viewModelScope.launch {
                    repository.updateCard(cardDomain)
                }
            }
        } else {
            val cardDomain = CardDomain(word = word, translatedWord = translate)
            viewModelScope.launch {
                repository.insertWordToDatabase(cardDomain)
            }
        }
    }

    private fun downloadTranslator() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.RUSSIAN)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        russianEnglishTranslator = Translation.getClient(options)
        if (!isTranslatorDownloaded) {
            var conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    russianEnglishTranslator!!.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener {
                            isTranslatorDownloaded = true
                            _isTranslatorDownloadedListener.value =
                                "Successfully downloaded Translator"
                        }
                        .addOnFailureListener { exception ->
                            isTranslatorDownloaded = false
                            _isTranslatorDownloadedListener.value = "Translator downloading failed"
                            Timber.e(exception)
                        }
                }
            }
        }
    }

    fun translateWord(text: String) {
        if (isTranslatorDownloaded) {
            russianEnglishTranslator!!.translate(text)
                .addOnSuccessListener { word ->
                    _translatedWord.value = word
                }
                .addOnFailureListener {
                    _translationException.value = it.message.toString()
                }
        } else {
            _translationException.value = "Translator wasn't downloaded, wait a minute"
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (russianEnglishTranslator != null) {
            russianEnglishTranslator!!.close()
        }
    }

    fun translatedWordDone() {
        _translatedWord.value = null
    }

    fun translatingExceptionDone() {
        _translationException.value = null
    }

    fun translatedListenerDone() {
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

    fun makeCardNull() {
        _wordCardDomain.value = null
    }


    companion object {

        fun provideFactory(
            assistedFactory: AssistedFactory,
            cardDomain: CardDomain?,
            downloaded: Boolean
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(cardDomain, downloaded) as T
            }

        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(cardDomain: CardDomain?, downloaded: Boolean): AddingViewModel
    }


}