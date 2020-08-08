package com.utkarshr.callblocker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SpamNumber::class], version = 1)
@TypeConverters(Converters::class)
abstract class SpamNumberDatabase : RoomDatabase() {

    abstract val dao: SpamNumberDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: SpamNumberDatabase? = null

        fun getInstance(context: Context): SpamNumberDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SpamNumberDatabase::class.java,
                        "spam_number_database"
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