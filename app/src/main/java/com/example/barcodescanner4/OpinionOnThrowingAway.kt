package com.example.barcodescanner4

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.DateFormat
import java.util.Calendar

data class OpinionOnThrowingAway(
    val kind_of_basket: String? = null,
    val descripton: String? = null,
    val date: Long = System.currentTimeMillis()
)

