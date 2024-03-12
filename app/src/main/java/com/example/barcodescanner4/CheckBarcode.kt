package com.example.barcodescanner4

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.barcodescanner4.ui.theme.BarCodeScanner4Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

open class CheckBarcode : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val PermissionCameraState = rememberPermissionState(Manifest.permission.CAMERA)
            val camera_active = remember { mutableStateOf(false) }
            val code = remember { mutableStateOf("0") }

            BarCodeScanner4Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (PermissionCameraState.status.isGranted) {
                        if (!camera_active.value) {
                            Greeting2(camera_active, code)
                        } else {
                            CameraScreen(
                                analyzerType = AnalyzerType.BARCODE,
                                input_code = code,
                                camera_active = camera_active
                            )
                        }

                    } else if (PermissionCameraState.status.shouldShowRationale) {
                        Text("Camera Permission permanently denied")
                    } else {
                        SideEffect {
                            PermissionCameraState.run { launchPermissionRequest() }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun Greeting2(camera: MutableState<Boolean>, code: MutableState<String>) {

        val color_white = Color(255,255,255)
        val color_grey = Color(74,76,92)
        val color_yellow = Color(216,160,86)
        val color_black = Color(51,60,75)
        val inputs_colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = color_yellow,
            unfocusedBorderColor = color_grey,
            unfocusedLabelColor = color_yellow,
            focusedLabelColor = color_black,
            cursorColor = color_black
        )

        val modifier_button = Modifier.padding(vertical = 15.dp)

        Scaffold(
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {},
                    icon = { Icon(Icons.Filled.Check, "Check this barcode in the database") },
                    text = { Text(text = "Check this barcode") },
                    containerColor = color_yellow,
                    contentColor = color_white,
                    modifier = Modifier.padding(10.dp).size(160.dp,55.dp)
                )
            }
        ){ innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                OutlinedButton(modifier = modifier_button, onClick = {camera.value = true}) {
                    Text(text = "Scan Barcode")
                }
                OutlinedTextField(
                    value = code.value,
                    onValueChange = {

                    }, enabled = true,
                    label = { Text(text = "Barcode") },
                    maxLines = 1,
                    colors = inputs_colors

                )

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview(){
    BarCodeScanner4Theme {
        //Greeting2("Android")
    }
}