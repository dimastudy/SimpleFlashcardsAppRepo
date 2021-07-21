package com.example.simpleflashcardsapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardsDao {

    @Query("select * from flashcardsTable")
    fun getAllCards(): LiveData<List<CardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(cardEntity: CardEntity)

    @Delete
    suspend fun deleteCard(cardEntity: CardEntity)


}


@Database(entities = [CardEntity::class], version = 1)
abstract class CardsDatabase : RoomDatabase() {
    abstract val cardsDao: CardsDao
}

private lateinit var INSTANCE: CardsDatabase

fun getDatabase(context: Context): CardsDatabase {
    if (!::INSTANCE.isInitialized) {
        INSTANCE = Room.databaseBuilder(
            context.applicationContext,
            CardsDatabase::class.java,
            "cardsdatabase"
        )
            .build()
    }

    return INSTANCE
}
