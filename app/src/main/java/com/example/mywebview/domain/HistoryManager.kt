package com.example.mywebview.domain

import com.example.mywebview.data.AppDataBase
import com.example.mywebview.data.HistoryDao
import com.example.mywebview.data.model.HistoryEntity
import com.example.mywebview.data.model.HistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryManager(dataBase: AppDataBase) {

    private val dao: HistoryDao = dataBase.historyDao()


    suspend fun invalidate(history: List<HistoryItem>) {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
            val entities = history.map { it.asEntity() }
            dao.insertAll(entities)
        }
    }

    suspend fun loadHistory(): List<HistoryItem> {
        return withContext(Dispatchers.IO) {
            val entities = dao.getAllUrls()
            entities.map { it.asModel() }
        }
    }


    private fun HistoryItem.asEntity(): HistoryEntity {
        return HistoryEntity(
            url = url,
            index = index
        )
    }

    private fun HistoryEntity.asModel(): HistoryItem {
        return HistoryItem(
            url = url,
            index = index
        )
    }
}