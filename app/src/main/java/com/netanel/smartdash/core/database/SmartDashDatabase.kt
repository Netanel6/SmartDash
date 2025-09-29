package com.netanel.smartdash.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.netanel.smartdash.core.database.dao.CoinDao
import com.netanel.smartdash.core.database.dao.WeatherDao
import com.netanel.smartdash.core.database.entity.CoinEntity
import com.netanel.smartdash.core.database.entity.WeatherEntity

@Database(
    entities = [WeatherEntity::class, CoinEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SmartDashDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun coinDao(): CoinDao
}
