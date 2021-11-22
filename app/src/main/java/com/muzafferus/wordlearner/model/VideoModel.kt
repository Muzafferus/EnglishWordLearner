package com.muzafferus.wordlearner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_table")
data class VideoModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "transcript")
    var transcript: String,
    @ColumnInfo(name = "url")
    var url: String,
    @ColumnInfo(name = "percent")
    var percent: Int? = 0
)
