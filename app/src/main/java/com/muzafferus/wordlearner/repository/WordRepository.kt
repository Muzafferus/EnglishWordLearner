package com.muzafferus.wordlearner.repository

import androidx.annotation.WorkerThread
import com.muzafferus.wordlearner.db.dao.WordDao
import com.muzafferus.wordlearner.model.WordModel
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: Flow<List<WordModel>> = wordDao.getAlphabetizedWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: WordModel) {
        wordDao.insert(word)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(word: WordModel) {
        wordDao.update(word)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(word: String) {
        wordDao.deleteByWord(word)
    }
}
