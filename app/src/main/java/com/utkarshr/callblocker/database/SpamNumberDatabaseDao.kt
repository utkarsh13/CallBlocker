package com.utkarshr.callblocker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface SpamNumberDatabaseDao {
    @Query("SELECT * FROM spam_number_table")
    fun getAll(): List<SpamNumber>?

    @Insert
    fun insert(number: SpamNumber)

    @Delete
    fun delete(number: SpamNumber)
}