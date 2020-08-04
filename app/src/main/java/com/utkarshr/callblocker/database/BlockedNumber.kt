package com.utkarshr.callblocker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_number_table")
data class BlockedNumber (
    val rawNumber: String,

    val regexType: RegexType,

    @PrimaryKey
    val regex: Regex
)