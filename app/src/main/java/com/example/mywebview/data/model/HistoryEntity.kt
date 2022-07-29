package com.example.mywebview.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "History",
    primaryKeys = ["url", "index"]
)
data class HistoryEntity(
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "index") val index: Int,
)