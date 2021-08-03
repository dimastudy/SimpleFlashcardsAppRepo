package com.example.simpleflashcardsapp.data

import androidx.lifecycle.Transformations
import com.example.simpleflashcardsapp.database.WordsDatabase
import com.example.simpleflashcardsapp.database.asDomainModel
import com.example.simpleflashcardsapp.domain.CardDomain
import javax.inject.Inject

class CardsRepository @Inject constructor(
    private val database: WordsDatabase
) {

    suspend fun insertWordToDatabase(cardDomain: CardDomain){
        database.cardsDao.insertCard(cardDomain.getDatabaseEntity())
    }

    suspend fun deleteFromDatabase(cardDomain: CardDomain){
        database.cardsDao.deleteCard(cardDomain.getDatabaseEntity())
    }


    suspend fun isCardExist(id: Int) : Boolean =
        database.cardsDao.isCardExist(id) != null

    suspend fun updateCard(cardDomain: CardDomain) {
        database.cardsDao.updateCard(cardDomain.getDatabaseEntity())
    }


    val cardsDatabase = database.cardsDao.getAllCards()
    val cardsDomain = Transformations.map(cardsDatabase){
        it.asDomainModel()
    }



}