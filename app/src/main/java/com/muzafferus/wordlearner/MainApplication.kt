package com.muzafferus.wordlearner

import android.app.Application
import com.muzafferus.articlelearner.db.database.ArticleRoomDatabase
import com.muzafferus.videolearner.db.database.VideoRoomDatabase
import com.muzafferus.wordlearner.db.database.WordRoomDatabase
import com.muzafferus.wordlearner.repository.ArticleRepository
import com.muzafferus.wordlearner.repository.VideoRepository
import com.muzafferus.wordlearner.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {

    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts

    private val wordDatabase by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    private val articleDatabase by lazy { ArticleRoomDatabase.getDatabase(this, applicationScope) }
    private val videoDatabase by lazy { VideoRoomDatabase.getDatabase(this, applicationScope) }
    val wordRepository by lazy { WordRepository(wordDatabase.wordDao()) }
    val articleRepository by lazy { ArticleRepository(articleDatabase.articleDao()) }
    val videoRepository by lazy { VideoRepository(videoDatabase.videoDao()) }

}