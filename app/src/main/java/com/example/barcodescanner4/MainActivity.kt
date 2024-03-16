package com.example.barcodescanner4

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.barcodescanner4.ui.theme.BarCodeScanner4Theme
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.coroutineScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            LaunchedEffect(Unit){
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "barcodes-database"
                ).build()

                val barcodeDao = db.userDao()
                val codes: List<Barcode> = barcodeDao.getAll()
                RAM_Database.barcodes_data.addAll(codes)

                for(c in codes){
                    RAM_Database.list_of_barcodes.add(c.code)
                }
                Toast.makeText(applicationContext, codes.toString(), Toast.LENGTH_LONG).show()
            }

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
    }

}