package com.muzafferus.wordlearner.db.dao

import androidx.room.*
import com.muzafferus.wordlearner.model.VideoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Query("SELECT * FROM video_table ORDER BY id ASC")
    fun getAlphabetizedVideos(): Flow<List<VideoModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(video: VideoModel)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(video: VideoModel)

    @Query("DELETE FROM video_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM video_table")
    suspend fun deleteAll()
}
