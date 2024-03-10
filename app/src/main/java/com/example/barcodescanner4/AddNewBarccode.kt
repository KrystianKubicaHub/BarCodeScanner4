package com.example.barcodescanner4

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.Firebase
import com.google.firebase.database.database


open class AddNewBarcode : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                ) {
                Greeting2()
            }
        }
    }


    @OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
    @Composable
    open fun Greeting2(modifier: Modifier = Modifier) {

        val camera_active = remember{ mutableStateOf(false) }

        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
        var analyzerType by remember { mutableStateOf(AnalyzerType.UNDEFINED) }

        val input_code = remember { mutableStateOf("") }
        if(RAM_Database.pass_the_code != null){
            input_code.value = RAM_Database.pass_the_code!!
        }
        val input_manufacturer = remember { mutableStateOf("") }
        val input_product_name = remember { mutableStateOf("") }
        val input_dumping_description = remember { mutableStateOf("") }

        val text_code_length = remember { mutableStateOf("") }
        val text_where_to_throw_statement = remember { mutableStateOf("") }
        val easily_Segreable_Text = remember { mutableStateOf("Easily Segregable") }
        val required_washing = remember { mutableStateOf(" Required washing: NO") }
        val error_info = remember { mutableStateOf("") }

        val color_selected_container = remember { mutableStateOf(Color.Black) }
        val color_easily_segreable_switch = remember { mutableStateOf(Color.Blue) }
        val color_required_washing_switch = remember { mutableStateOf(Color.Red) }

        val switchV_easily_segreable = remember { mutableStateOf(true) }
        val switchV_required_washing = remember { mutableStateOf(false) }

        val label_code = "Code"
        val label_manufacturer = "Manufacturer"
        val label_product_name = "Product Name"
        val label_description = "Describe how to segregate this product"

        val text_confirm_button = "Confirm"
        val text_where_to_throw_question = "In which container should the product be thrown?"
        val text_open_scanner = "Use Scanner"

        /// switch responsible for the type of selected segregation
        val value_to_be_assigned_1 = "Easily Segregable"
        val value_to_be_assigned_2 = "Complicated Segregation"

        /// switch of whether you have to wash the product
        val value_to_be_assigned_3 = "Required washing: YES"
        val value_to_be_assigned_4 = "Required washing: NO"

        /// values assigned to text_where_to_throw_statement when the garbage can is selected
        val value_to_be_assigned_5 = "This waste should be put in the paper garbage can"
        val value_to_be_assigned_6 = "This waste should be put in the plastic garbage can"
        val value_to_be_assigned_7 = "This waste should be put in the glass garbage can"
        val value_to_be_assigned_8 = "This waste should be put in the garbage can for mixed waste"
        val value_to_be_assigned_9 = "This waste should be put in the bio garbage can"

        val modifier_element = Modifier
            .padding(start = 40.dp, end = 40.dp, top = 25.dp)
            .fillMaxWidth()



        if (cameraPermissionState.status.isGranted){
            if (!camera_active.value){
                Column {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = modifier_element
                                .background(
                                    shape = RoundedCornerShape(20.dp), brush = Brush.linearGradient(
                                        listOf(Color(96, 239, 255), Color(0, 97, 255)),
                                        start = Offset(15f, 50.0f),
                                        end = Offset(6f, 100.0f)
                                    )
                                )
                                .clickable(onClick = { analyzerType = AnalyzerType.BARCODE; camera_active.value = true}),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.camera50),
                                contentDescription = "",
                                tint = Color(225, 225, 225),
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(40.dp)
                            )
                            Text(
                                text = text_open_scanner,
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp,
                                color = Color(225, 225, 225),
                                modifier = Modifier.padding(start = 20.dp)
                            )
                        }
                        if (error_info.value != "") {
                            OutlinedTextField(
                                value = error_info.value,
                                onValueChange = {},
                                enabled = false,
                                modifier = modifier_element,
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = Color.Red,
                                    disabledBorderColor = Color.Red
                                )
                            )
                        }

                        Row(
                            modifier = modifier_element,
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = text_code_length.value, fontSize = 20.sp)
                            OutlinedTextField(
                                value = input_code.value,
                                onValueChange = {
                                    text_code_length.value = it.length.toString()
                                    input_code.value = it
                                }, enabled = true,
                                label = { Text(label_code) },
                                maxLines = 1

                            )
                        }
                        OutlinedTextField(
                            value = input_manufacturer.value,
                            onValueChange = {
                                input_manufacturer.value = it
                            }, enabled = true,
                            modifier = modifier_element,
                            label = { Text(label_manufacturer) },
                            maxLines = 1
                        )
                        OutlinedTextField(
                            value = input_product_name.value,
                            onValueChange = {
                                input_product_name.value = it
                            }, enabled = true,
                            modifier = modifier_element,
                            label = { Text(label_product_name) },
                            maxLines = 1
                        )
                        Row(
                            modifier = modifier_element
                                .background(Color.LightGray, shape = RoundedCornerShape(10.dp)),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Switch(modifier = Modifier.padding(start = 15.dp),
                                checked = switchV_easily_segreable.value,
                                onCheckedChange = {
                                    switchV_easily_segreable.value = it

                                    if (color_easily_segreable_switch.value == Color.Blue) {
                                        color_easily_segreable_switch.value = Color.Red
                                        easily_Segreable_Text.value = value_to_be_assigned_2
                                    } else {
                                        easily_Segreable_Text.value = value_to_be_assigned_1
                                        color_easily_segreable_switch.value = Color.Blue
                                    }
                                }
                            )
                            Text(
                                text = easily_Segreable_Text.value,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .padding(start = 40.dp),
                                color = color_easily_segreable_switch.value
                            )
                        }
                        Row(
                            modifier = modifier_element
                                .background(Color.LightGray, shape = RoundedCornerShape(10.dp)),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Switch(modifier = Modifier.padding(start = 15.dp),
                                checked = switchV_required_washing.value,
                                onCheckedChange = {
                                    switchV_required_washing.value = it

                                    if (it) {
                                        required_washing.value = value_to_be_assigned_3
                                        color_required_washing_switch.value = Color.Blue
                                    } else {
                                        required_washing.value = value_to_be_assigned_4
                                        color_required_washing_switch.value = Color.Red
                                    }
                                }
                            )
                            Text(
                                text = required_washing.value,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .padding(start = 40.dp),
                                color = color_required_washing_switch.value
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier_element
                        ) {
                            if (switchV_easily_segreable.value) {
                                Text(
                                    text = text_where_to_throw_question,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                Row(
                                    modifier = Modifier.padding(5.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Delete,
                                        contentDescription = "",
                                        tint = Color.Blue,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .combinedClickable(
                                                onClick = {
                                                    text_where_to_throw_statement.value =
                                                        value_to_be_assigned_5

                                                    color_selected_container.value = Color.Blue
                                                }
                                            )
                                    )
                                    Icon(imageVector = Icons.Default.Delete,
                                        contentDescription = "",
                                        tint = Color.Yellow,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .combinedClickable(
                                                onClick = {
                                                    text_where_to_throw_statement.value =
                                                        value_to_be_assigned_6

                                                    color_selected_container.value = Color.Yellow
                                                }
                                            ))
                                    Icon(imageVector = Icons.Default.Delete,
                                        contentDescription = "",
                                        tint = Color.Green,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .combinedClickable(
                                                onClick = {
                                                    text_where_to_throw_statement.value =
                                                        value_to_be_assigned_7

                                                    color_selected_container.value = Color.Green
                                                }
                                            ))
                                    Icon(imageVector = Icons.Default.Delete,
                                        contentDescription = "",
                                        tint = Color.Black,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .combinedClickable(
                                                onClick = {
                                                    text_where_to_throw_statement.value =
                                                        value_to_be_assigned_8

                                                    color_selected_container.value = Color.Black
                                                }
                                            ))
                                    Icon(imageVector = Icons.Default.Delete,
                                        contentDescription = "",
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .combinedClickable(
                                                onClick = {
                                                    text_where_to_throw_statement.value =
                                                        value_to_be_assigned_9

                                                    color_selected_container.value = Color.Red
                                                }
                                            ))
                                }

                                if (text_where_to_throw_statement.value !== "") {
                                    Text(
                                        text = text_where_to_throw_statement.value,
                                        color = color_selected_container.value,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            start = 25.dp,
                                            end = 25.dp,
                                            top = 10.dp,
                                            bottom = 10.dp
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                OutlinedTextField(
                                    value = input_dumping_description.value,
                                    onValueChange = {
                                        input_dumping_description.value = it
                                    }, enabled = true,
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 40.dp), shape = RoundedCornerShape(20.dp),
                                    label = {
                                        Text(
                                            text = label_description,
                                            color = Color.Black, fontWeight = FontWeight.Bold
                                        )
                                    }
                                )
                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Button(
                                onClick = {
                                    onConfirm(
                                        input_code.value,
                                        input_manufacturer.value,
                                        input_product_name.value,
                                        switchV_easily_segreable.value,
                                        color_selected_container.value.toString(),
                                        input_dumping_description.value,
                                        error_info
                                    )
                                },
                                modifier = modifier_element.padding(bottom = 35.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                            ) {
                                Text(text = text_confirm_button)
                            }
                        } } }}
            else{
                CameraScreen(analyzerType, input_code, camera_active)
            }
        } else if (cameraPermissionState.status.shouldShowRationale) {
            Text("Camera Permission permanently denied")
        } else {
            SideEffect {
                cameraPermissionState.run { launchPermissionRequest() }
            }
            Text("No Camera Permission")
        }





    }
    fun onConfirm(
        code: String, manufacturer: String, product_name: String, switch_state: Boolean,
        selected_basket: String, description: String, error_info: MutableState<String>
    ) : Int{ // switch_state == true -> user selected one of the baskets

        if(code.length != 13){
            error_info.value = "The length of the barcode must be 13"
            return 0
        }
        if(!code.isDigitsOnly()){
            error_info.value = "Barcode must consist only of numbers"
            return 0
        }
        if(manufacturer == ""){
            error_info.value = "Enter manufacturer"
            return 0
        }
        if(product_name == ""){
            error_info.value = "Enter product name"
            return 0
        }
        if(switch_state && selected_basket ==""){
            error_info.value = "You must select one of the baskets"
            return 0
        }

        val Evaluate = EvaluateTheNewCode(code.toLong())
        if (Evaluate.check_if_code_in_registry()) {
            7 + 6
        } else {
            val barcode: Barcode
            if (switch_state) {
                val users_opinion = OpinionOnThrowingAway(
                    kind_of_basket = selected_basket,
                    descripton = null
                )
                barcode = Evaluate.getDataFromBarcode(
                    new_opinion = users_opinion,
                    required_washing = false
                )!!

                barcode.manufacturer = manufacturer
                barcode.product_name = product_name

                val database =
                    Firebase.database("https://barcodescanner4-cbe9f-default-rtdb.europe-west1.firebasedatabase.app/")
                val myRef = database.getReference("barcodes").child(barcode.code.toString())

                myRef.setValue(barcode)


            } else {
                val users_opinion = OpinionOnThrowingAway(
                    kind_of_basket = null,
                    descripton = description
                )
                barcode = Evaluate.getDataFromBarcode(
                    new_opinion = users_opinion,
                    required_washing = false
                )!!


                barcode.manufacturer = manufacturer
                barcode.product_name = product_name

                val database =
                    Firebase.database("https://barcodescanner4-cbe9f-default-rtdb.europe-west1.firebasedatabase.app/")
                val myRef = database.getReference("barcodes").child(barcode.code.toString())

                myRef.setValue(barcode)
            }

        }

        return 1
    }

    @Preview
    @Composable
    fun Prev(){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ){
            Greeting2()
        }
    }

}


