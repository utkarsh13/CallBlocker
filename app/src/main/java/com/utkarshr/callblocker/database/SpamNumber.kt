package com.utkarshr.callblocker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spam_number_table")
data class SpamNumber (
    val rawNumber: String,

    val regexType: RegexType,

    @PrimaryKey
    val regex: Regex
)