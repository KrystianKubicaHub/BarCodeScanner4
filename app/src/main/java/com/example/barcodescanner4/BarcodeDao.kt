package com.example.barcodescanner4

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BarcodeDao{
    @Query("SELECT * FROM barcode")
    suspend fun getAll(): List<Barcode>

    @Query("SELECT * FROM barcode WHERE code IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Barcode>

    @Query("SELECT * FROM barcode WHERE country_of_origin LIKE :first AND " +
            "country_of_origin LIKE :last LIMIT 1")
    suspend fun findByName(first: String, last: String): Barcode

    @Insert
    suspend fun insertAll(vararg barcodes: Barcode)

    @Delete
    suspend fun delete(barcodes: Barcode)
}