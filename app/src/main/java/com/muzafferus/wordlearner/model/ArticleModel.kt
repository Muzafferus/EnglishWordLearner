package com.muzafferus.wordlearner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_table")
data class ArticleModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "name")
    var name:String,
    @ColumnInfo(name = "description")
    var description:String,
    @ColumnInfo(name = "percent")
    var percent:Int?=0
)
