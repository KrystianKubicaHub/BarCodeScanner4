package com.example.barcodescanner4

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent{
            Database.Initialize()

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
fun Greeting() {
    val mContext = LocalContext.current

    val barcodesText = remember{ mutableStateOf("") }

    val modifier_button = Modifier.padding(horizontal = 40.dp, vertical = 15.dp)

    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedButton(onClick = { mContext.startActivity(Intent(mContext, AddNewBarcode::class.java)) },
            modifier = modifier_button){
            Text(text = "Add new barcode")
        }
        OutlinedButton(onClick = { mContext.startActivity(Intent(mContext, CheckBarcode::class.java)) },
            modifier = modifier_button){
            Text(text = "Check barcode")
        }
        OutlinedButton(onClick = {barcodesText.value = RAM_Database.list_of_barcodes.toString()}, modifier = modifier_button){
            Text(text = "Show Database")
        }
        OutlinedTextField(value = barcodesText.value , onValueChange = {}, modifier = modifier_button)

        Database.ExecuteTimeView()
    }

}