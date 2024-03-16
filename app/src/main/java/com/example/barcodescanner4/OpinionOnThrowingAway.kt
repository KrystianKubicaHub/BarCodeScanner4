package com.example.barcodescanner4

import java.text.DateFormat
import java.util.Calendar


data class OpinionOnThrowingAway(
    val kind_of_basket: String? = null,
    val descripton: String? = null,
    val date: Long = System.currentTimeMillis()
)