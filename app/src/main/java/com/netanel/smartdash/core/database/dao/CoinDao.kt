package com.netanel.smartdash.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.netanel.smartdash.core.database.entity.CoinEntity

@Dao
interface CoinDao {
    @Query("SELECT * FROM coins ORDER BY timestamp DESC LIMIT :limit")
    suspend fun get(limit: Int): List<CoinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<CoinEntity>)
}
