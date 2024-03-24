package com.example.barcodescanner4

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


object Database {
    private var my_time: Int = 0

    @Composable
     fun Initialize(){
        val context = LocalContext.current
        LaunchedEffect(Unit){
            main(context)
        }

    }
    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun main(context: Context) {
            GlobalScope.launch{
                val scopeSQL : Job = launch(context = Dispatchers.Default){
                    loadDataFromSQL(context)
                }
                scopeSQL.join()



                val scopeFireBase : Job = launch(context = Dispatchers.Default){
                    loadDataFromFireBase(RAM_Database.list_of_barcodes.size, context)
                }
                scopeFireBase.join()

            }
    }

    fun pushToFireBase(barcode: Barcode){
        val database =
            Firebase.database("https://barcodescanner4-cbe9f-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference("barcodes").child(RAM_Database.list_of_barcodes.size.toString())

        myRef.setValue(barcode)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadDataFromFireBase(index: Int, context: Context){
        val database =
            Firebase.database("https://barcodescanner4-cbe9f-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference("barcodes").child(index.toString())



        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){
                    var code_received: Long? = null
                    var country_received: String? = null
                    var manufacturer_received: String? = null
                    var opinions_received: List<OpinionOnThrowingAway>? = null
                    var required_washing_received: Boolean? = null
                    var product_name_received: String? = null

                    for(i in snapshot.children){
                        if(i.key == "code"){
                            code_received = i.getValue<Long>()
                        }
                        if(i.key == "country_of_origin"){
                            country_received = i.getValue<String>()
                        }
                        if(i.key == "manufacturer"){
                            manufacturer_received = i.getValue<String>()
                        }
                        if(i.key == "opinion_on_throwing_away"){
                            opinions_received = i.getValue<List<OpinionOnThrowingAway>>()
                        }
                        if(i.key == "product_name"){
                            product_name_received = i.getValue<String>()
                        }
                        if(i.key == "requiresWashing"){
                            required_washing_received = i.getValue<Boolean>()
                        }
                    }

                    if(code_received!=null&&
                        country_received!=null&&
                        manufacturer_received!=null&&
                        product_name_received!=null&&
                        opinions_received!=null&&
                        required_washing_received!=null){

                        val newBarcode = Barcode(
                            code = code_received,
                            country_of_origin = country_received,
                            manufacturer = manufacturer_received,
                            product_name = product_name_received,
                            opinion_on_throwing_away = opinions_received,
                            requiresWashing = required_washing_received
                        )
                        RAM_Database.barcodes_data.add(newBarcode)
                        RAM_Database.list_of_barcodes.add(newBarcode.code)



                        Log.e("Piastów4", newBarcode.opinion_on_throwing_away.toString())

                        val db = Room.databaseBuilder(
                            context,
                            AppDatabase::class.java, "barcodes-database"
                        ).fallbackToDestructiveMigration().build()

                        GlobalScope.launch {
                            val barcodeDao = db.userDao()

                            barcodeDao.insertAll(newBarcode)


                            loadDataFromFireBase(index+1, context)
                        }
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }

        })

    }

    private suspend fun loadDataFromSQL(context: Context){
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "barcodes-database"
        ).fallbackToDestructiveMigration().build()

        val barcodeDao = db.userDao()
        val codes: List<Barcode> = barcodeDao.getAll()

        RAM_Database.barcodes_data.addAll(codes)

        for(c in codes){
            RAM_Database.list_of_barcodes.add(c.code)
        }


    }


    @Composable
    fun ExecuteTimeView(){
        val time = remember { mutableStateOf("") }
        Row(
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = {time.value = RAM_Database.list_of_barcodes.size.toString()}, modifier = Modifier.size(width = 150.dp, height = 100.dp)){
                Text(text = time.value, fontSize = 20.sp)
            }
        }
    }




}