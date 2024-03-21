package com.example.barcodescanner4

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
object RandomFuns{

    private var my_time: Int
    init{
         my_time = 0
        GlobalScope.launch {
            //executeTime()
        }
    }

    suspend fun executeTime(){
        delay(1000)
        my_time = my_time + 1
        executeTime()
    }

    @Composable
    fun executeTimeView(){
        val time = remember {mutableStateOf("")}
        Row(
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = {time.value = my_time.toString()}, modifier = Modifier.size(width = 150.dp, height = 100.dp)){
                Text(text = time.value, fontSize = 20.sp)
            }
        }
    }
}