package com.example.barcodescanner4

data class Barcode(
    val code: Long,
    val country_of_origin: String,
    var manufacturer: String,
    var product_name: String,
    val opinion_on_throwing_away: List<OpinionOnThrowingAway>,
    val requiresWashing: Boolean)