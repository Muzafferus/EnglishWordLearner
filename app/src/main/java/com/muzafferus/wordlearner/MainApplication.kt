package com.muzafferus.wordlearner

import android.app.Application
import com.muzafferus.articlelearner.db.database.ArticleRoomDatabase
import com.muzafferus.wordlearner.db.database.WordRoomDatabase
import com.muzafferus.wordlearner.repository.ArticleRepository
import com.muzafferus.wordlearner.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {

    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts

    val wordDatabase by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    val articleDatabase by lazy { ArticleRoomDatabase.getDatabase(this, applicationScope) }
    val wordRepository by lazy { WordRepository(wordDatabase.wordDao()) }
    val articleRepository by lazy { ArticleRepository(articleDatabase.articleDao()) }

}