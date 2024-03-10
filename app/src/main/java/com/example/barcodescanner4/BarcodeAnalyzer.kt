package com.example.barcodescanner4

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.MutableState
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(
    private val context: Context,
    private val input_code: MutableState<String>,
    private var analyzerType: AnalyzerType,
    private var camera_active: MutableState<Boolean>
) : ImageAnalysis.Analyzer {


    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    private val scanner = BarcodeScanning.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image
            ?.let { image ->
                scanner.process(
                    InputImage.fromMediaImage(
                        image, imageProxy.imageInfo.rotationDegrees
                    )
                ).addOnSuccessListener { barcode ->
                    barcode?.takeIf { it.isNotEmpty() }
                        ?.mapNotNull { it.rawValue }
                        ?.joinToString(",")
                        ?.let { input_code.value = it; scanner.close(); analyzerType = AnalyzerType.UNDEFINED;
                            imageProxy.close(); camera_active.value = false }
                }.addOnCompleteListener {
                    imageProxy.close()
                }
            }
    }
}