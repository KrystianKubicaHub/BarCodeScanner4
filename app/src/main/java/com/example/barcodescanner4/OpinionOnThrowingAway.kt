package com.example.barcodescanner4

import java.text.DateFormat
import java.util.Calendar


data class OpinionOnThrowingAway(
    val kind_of_basket: String?,
    val descripton: String?,
    val date: Long = System.currentTimeMillis()
)