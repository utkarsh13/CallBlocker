package com.utkarshr.callblocker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface BlockedNumberDatabaseDao {
    @Query("SELECT * FROM blocked_number_table")
    fun getAll(): List<BlockedNumber?>?

    @Insert
    fun insert(number: BlockedNumber)

    @Delete
    fun delete(number: BlockedNumber)
}