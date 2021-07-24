package com.example.simpleflashcardsapp.data

import com.example.simpleflashcardsapp.database.CardEntity
import com.example.simpleflashcardsapp.database.CardsDatabase
import javax.inject.Inject

class CardsRepository @Inject constructor(
    private val database: CardsDatabase
) {

    suspend fun insertWordToDatabase(cardEntity: CardEntity){
        database.cardsDao.insertCard(cardEntity)
    }

    val cards = database.cardsDao.getAllCards()


}