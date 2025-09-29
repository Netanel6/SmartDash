package com.netanel.smartdash.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.netanel.smartdash.core.database.entity.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather ORDER BY timestamp DESC LIMIT 1")
    suspend fun get(): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WeatherEntity)
}
