package com.example.barcodescanner4

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Barcode(
    @PrimaryKey val code: Long,
    @ColumnInfo(name = "country_of_origin") var country_of_origin: String,
    @ColumnInfo(name = "manufacturer") var manufacturer: String,
    @ColumnInfo(name = "product_name") var product_name: String,
    @ColumnInfo(name = "opinion_on_throwing_away") var opinion_on_throwing_away: List<OpinionOnThrowingAway>,
    @ColumnInfo(name = "requiresWashing") var requiresWashing: Boolean)