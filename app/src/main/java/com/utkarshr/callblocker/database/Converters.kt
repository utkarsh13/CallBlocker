package com.utkarshr.callblocker.database

import androidx.room.TypeConverter

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromRegexType(value: RegexType): Int {
            return value.value
        }

        @TypeConverter
        @JvmStatic
        fun toRegexType(value: Int): RegexType {
            return RegexType.values()[value]
        }

        @TypeConverter
        @JvmStatic
        fun fromRegex(value: Regex): String {
            return value.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toRegex(value: String): Regex {
            return Regex(value)
        }
    }
}