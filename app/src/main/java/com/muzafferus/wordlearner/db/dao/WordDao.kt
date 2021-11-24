package com.muzafferus.wordlearner.db.dao

import androidx.room.*
import com.muzafferus.wordlearner.model.WordModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): Flow<List<WordModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: WordModel)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(word: WordModel)

    @Query("DELETE FROM word_table WHERE word = :word")
    suspend fun deleteByWord(word: String)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}
