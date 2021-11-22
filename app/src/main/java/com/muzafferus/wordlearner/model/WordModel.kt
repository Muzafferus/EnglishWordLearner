package com.muzafferus.wordlearner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
data class WordModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "word")
    var word: String,
    @ColumnInfo(name = "type")
    var type: Int
)
