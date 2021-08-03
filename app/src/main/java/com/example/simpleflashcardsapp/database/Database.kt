package com.example.simpleflashcardsapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardsDao {

    @Query("select * from wordstable")
    fun getAllCards(): LiveData<List<WordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(cardEntity: WordEntity)

    @Delete
    suspend fun deleteCard(cardEntity: WordEntity)

    @Update(entity = WordEntity::class)
    suspend fun updateCard(cardEntity: WordEntity)

    @Query("select * from wordstable where id = :id")
    suspend fun isCardExist(id: Int): WordEntity?

    @Query("delete from wordstable")
    suspend fun clear()

}


@Database(
    version = 1,
    entities = [WordEntity::class],
)
abstract class WordsDatabase : RoomDatabase() {
    abstract val cardsDao: CardsDao
}

private lateinit var INSTANCE: WordsDatabase

fun getDatabase(context: Context): WordsDatabase {
    if (!::INSTANCE.isInitialized) {
        INSTANCE = Room.databaseBuilder(
            context.applicationContext,
            WordsDatabase::class.java,
            "wordsdatabase"
        )
            .build()
    }

    return INSTANCE
}


