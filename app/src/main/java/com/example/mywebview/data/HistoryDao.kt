package com.example.mywebview.data

import androidx.room.*
import com.example.mywebview.data.model.HistoryEntity

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(urls: List<HistoryEntity>)

    @Query("DELETE FROM History")
    fun deleteAll()

    @Query("DELETE FROM History WHERE `index` = (SELECT `index` FROM History ORDER BY `index` DESC LIMIT 1)")
    fun deleteLast()

    @Delete
    fun delete(urls: List<HistoryEntity>)

    @Query("SELECT * FROM History ORDER BY `index` ASC")
    fun getAllUrls(): List<HistoryEntity>
}