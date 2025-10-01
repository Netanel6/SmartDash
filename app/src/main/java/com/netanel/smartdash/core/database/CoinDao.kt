package com.netanel.smartdash.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CoinDao {
    @Query("SELECT * FROM coin_cache WHERE queryKey = :queryKey ORDER BY orderIndex ASC")
    suspend fun getCoins(queryKey: String): List<CoinCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(coins: List<CoinCacheEntity>)

    @Query("DELETE FROM coin_cache WHERE queryKey = :queryKey")
    suspend fun deleteForQuery(queryKey: String)

    @Transaction
    suspend fun replaceForQuery(queryKey: String, coins: List<CoinCacheEntity>) {
        deleteForQuery(queryKey)
        if (coins.isNotEmpty()) {
            upsertAll(coins)
        }
    }
}
