package com.netanel.smartdash.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WeatherEntity::class, CoinCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SmartDashDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun coinDao(): CoinDao
}
