package com.example.barcodescanner4

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.barcodescanner4.ui.theme.BarCodeScanner4Theme
import com.google.firebase.Firebase
import com.google.firebase.database.database

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
        }
    }
}


@Composable
fun Greeting(name: String? = null, modifier: Modifier = Modifier) {
    val mContext = LocalContext.current
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
    Button(onClick = {
        mContext.startActivity(Intent(mContext, AddNewBarcode::class.java))
    }, modifier = modifier.padding(50.dp)){
        Text(text = "Add new barcode")
    }
}