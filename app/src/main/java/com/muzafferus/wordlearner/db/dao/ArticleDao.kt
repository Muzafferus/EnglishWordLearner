package com.muzafferus.wordlearner.db.dao

import androidx.room.*
import com.muzafferus.wordlearner.model.ArticleModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article_table ORDER BY id ASC")
    fun getAlphabetizedArticles(): Flow<List<ArticleModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(article: ArticleModel)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(article: ArticleModel)

    @Query("DELETE FROM article_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM article_table")
    suspend fun deleteAll()
}
