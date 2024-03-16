package com.example.barcodescanner4

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converter::class)
@Database(entities = [Barcode::class], version = 11)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): BarcodeDao
}