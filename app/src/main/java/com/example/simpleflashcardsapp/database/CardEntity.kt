package com.example.simpleflashcardsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "flashcardsTable")
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val word: String,
    val translatedWord: String
) {
}