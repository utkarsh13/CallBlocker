package com.utkarshr.callblocker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [BlockedNumber::class], version = 1)
@TypeConverters(Converters::class)
abstract class BlockedNumberDatabase : RoomDatabase() {

    abstract val dao: BlockedNumberDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: BlockedNumberDatabase? = null

        fun getInstance(context: Context): BlockedNumberDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BlockedNumberDatabase::class.java,
                        "blocked_number_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}