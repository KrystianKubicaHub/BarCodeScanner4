package com.example.barcodescanner4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Error

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val scope = rememberCoroutineScope()
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

                getValueFromFirebase(RAM_Database.list_of_barcodes.size ,applicationContext, scope)
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


fun getValueFromFirebase(index : Int, context: Context, scope: CoroutineScope){
    val database =
        Firebase.database("https://barcodescanner4-cbe9f-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("barcodes").child(index.toString())
    // Read from the database
    myRef.addValueEventListener(object: ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.value != null){
                var code_received: Long? = null
                var country_received: String? = null
                var manufacturer_recived: String? = null
                var opinions_recived: List<OpinionOnThrowingAway>? = null
                var required_washing_recived: Boolean? = null
                var product_name_recived: String? = null

                    for(i in snapshot.children){
                        if(i.key == "code"){
                            code_received = i.getValue<Long>()
                        }
                        if(i.key == "country_of_origin"){
                            country_received = i.getValue<String>()
                        }
                        if(i.key == "manufacturer"){
                            manufacturer_recived = i.getValue<String>()
                        }
                        if(i.key == "opinion_on_throwing_away"){
                            opinions_recived = i.getValue<List<OpinionOnThrowingAway>>()
                        }
                        if(i.key == "product_name"){
                            product_name_recived = i.getValue<String>()
                        }
                        if(i.key == "requiresWashing"){
                            required_washing_recived = i.getValue<Boolean>()
                        }
                    }

                if(code_received!=null&&
                    country_received!=null&&
                    manufacturer_recived!=null&&
                    product_name_recived!=null&&
                    opinions_recived!=null&&
                    required_washing_recived!=null){

                    val newBarcode = Barcode(
                        code = code_received,
                        country_of_origin = country_received,
                        manufacturer = manufacturer_recived,
                        product_name = product_name_recived,
                        opinion_on_throwing_away = opinions_recived,
                        requiresWashing = required_washing_recived
                    )
                    RAM_Database.barcodes_data.add(newBarcode)
                    RAM_Database.list_of_barcodes.add(newBarcode.code)

                    val db = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java, "barcodes-database"
                    ).build()


                    scope.launch {
                        val barcodeDao = db.userDao()
                        barcodeDao.insertAll(newBarcode)
                        getValueFromFirebase(index+1, context, scope)
                    }



                }
            }



        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
        }

    })
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