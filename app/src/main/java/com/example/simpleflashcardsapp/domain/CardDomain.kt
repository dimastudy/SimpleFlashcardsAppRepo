package com.example.simpleflashcardsapp.domain

import android.os.Parcelable
import com.example.simpleflashcardsapp.database.WordEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardDomain(
    var id: Int = 0,
    val word: String,
    val translatedWord: String,
    val isLearned: Boolean = false
) : Parcelable {

    fun getDatabaseEntity() : WordEntity =
        WordEntity(
            id = this.id,
            word = this.word,
            translatedWord = this.translatedWord,
            isLearned = this.isLearned
        )

    fun resetInstance() : CardDomain =
        CardDomain(
            id = this.id,
            word = this.word,
            translatedWord = this.translatedWord,
            isLearned = false
        )

    fun makeTrueLearned() : CardDomain =
        CardDomain(
            id = this.id,
            word = this.word,
            translatedWord = this.translatedWord,
            isLearned = true
        )

}