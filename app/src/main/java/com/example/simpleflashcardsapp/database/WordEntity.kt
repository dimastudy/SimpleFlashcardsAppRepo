package com.example.simpleflashcardsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simpleflashcardsapp.domain.CardDomain


@Entity(tableName = "wordstable")
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val word: String,
    val translatedWord: String,
    val isLearned: Boolean = false
)


fun List<WordEntity>.asDomainModel(): List<CardDomain> {
    return map {
        CardDomain(
            id = it.id,
            word = it.word,
            translatedWord = it.translatedWord,
            isLearned =  it.isLearned
        )
    }
}