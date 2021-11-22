package com.muzafferus.videolearner.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.muzafferus.wordlearner.db.dao.VideoDao
import com.muzafferus.wordlearner.model.VideoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(VideoModel::class), version = 1, exportSchema = false)
abstract class VideoRoomDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    private class VideoDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val videoDao = database.videoDao()

                    // Delete all content here.
                    videoDao.deleteAll()

                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: VideoRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): VideoRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VideoRoomDatabase::class.java,
                    "video_database"
                )
                    .addCallback(VideoDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
