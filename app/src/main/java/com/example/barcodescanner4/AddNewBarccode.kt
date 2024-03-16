package com.example.barcodescanner4

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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


    @OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class,
    )
    @Composable
    open fun Greeting2(modifier: Modifier = Modifier) {

        val scope = rememberCoroutineScope()

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
        val easily_Segreable_Text = remember { mutableStateOf("Easily Segregable") }
        val required_washing = remember { mutableStateOf(" Required washing: NO") }
        val error_info = remember { mutableStateOf("") }
        val in_which_containter_to_throw =
            remember{ mutableStateOf("In which container should the product be thrown?") }

        val selected_container = remember{ mutableStateOf("")}

        val color_white = Color(255,255,255)
        val color_grey = Color(74,76,92)
        val color_yellow = Color(216,160,86)
        val color_black = Color(51,60,75)



        val color_selected_container = remember { mutableStateOf(color_white) }
        val color_easily_segreable_switch = remember { mutableStateOf(color_black) }
        val color_required_washing_switch = remember { mutableStateOf(color_yellow) }

        val switchV_easily_segreable = remember { mutableStateOf(true) }
        val switchV_required_washing = remember { mutableStateOf(false) }

        val icon_size_clicked = 75.dp
        val icon_size_common = 60.dp

        val icon1_size = remember { mutableStateOf(icon_size_common) }
        val icon2_size = remember { mutableStateOf(icon_size_common) }
        val icon3_size = remember { mutableStateOf(icon_size_common) }
        val icon4_size = remember { mutableStateOf(icon_size_common) }
        val icon5_size = remember { mutableStateOf(icon_size_common) }


        val inputs_colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = color_yellow,
            unfocusedBorderColor = color_white,
            unfocusedLabelColor = color_yellow,
            focusedLabelColor = color_white,
            cursorColor = color_black
        )
        val switch_colors = SwitchDefaults.colors(
            checkedThumbColor = color_white,
            checkedTrackColor = color_yellow
        )


        val label_code = "Code"
        val label_manufacturer = "Manufacturer"
        val label_product_name = "Product Name"
        val label_description = "Describe how to segregate this product"

        val text_confirm_button = "Confirm"
        val text_open_scanner = "Use Scanner"

        /// switch responsible for the type of selected segregation
        val value_to_be_assigned_1 = "Easily Segregable"
        val value_to_be_assigned_2 = "Complicated Segregation"

        /// switch of whether you have to wash the product
        val value_to_be_assigned_3 = "Required washing: YES"
        val value_to_be_assigned_4 = "Required washing: NO"

        /// values assigned to text_where_to_throw_statement when the garbage can is selected
        val value_to_be_assigned_5 = "This waste should be put in the paper garbage can"
        val value_to_be_assigned_6 = "This waste should be put in the plastic can"
        val value_to_be_assigned_7 = "This waste should be put in the glass garbage can"
        val value_to_be_assigned_8 = "This waste should be put in the for can mixed"
        val value_to_be_assigned_9 = "This waste should be put in the bio garbage can"

        val modifier_element = Modifier
            .padding(start = 40.dp, end = 40.dp, top = 25.dp)
            .fillMaxWidth()

        val basket_icon_padding = 10.dp



        if (cameraPermissionState.status.isGranted){
            if (!camera_active.value){
                Scaffold(
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = { scope.launch{
                                onConfirm(
                                    input_code.value,
                                    input_manufacturer.value,
                                    input_product_name.value,
                                    switchV_easily_segreable.value,
                                    selected_container.value,
                                    input_dumping_description.value,
                                    error_info,
                                    switchV_required_washing.value,
                                )
                            }},
                            icon = { Icon(Icons.Filled.Check, "Extended floating action button.") },
                            text = { Text(text = text_confirm_button) },
                            containerColor = color_yellow,
                            contentColor = color_white,
                            modifier = Modifier.padding(10.dp).size(160.dp,55.dp)
                        )
                    }
                ) { innerPadding ->
                    val fakepaddking = innerPadding

                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize().background(color = color_grey)
                    ) {
                        Row(
                            modifier = modifier_element,
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    analyzerType = AnalyzerType.BARCODE; camera_active.value = true
                                },
                                icon = { Icon(
                                    painter = painterResource(id = R.drawable.camera50),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp),
                                    tint = color_white)
                                       },
                                text = { Text(text = text_open_scanner,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = color_white) },
                                containerColor = color_black,
                                contentColor = color_white,
                                modifier = Modifier.size(220.dp, 60.dp)
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
                            Text(text = text_code_length.value, fontSize = 20.sp, color = color_yellow)
                            OutlinedTextField(
                                value = input_code.value,
                                onValueChange = {
                                    text_code_length.value = it.length.toString()
                                    input_code.value = it
                                }, enabled = true,
                                label = { Text(label_code) },
                                maxLines = 1,
                                colors = inputs_colors

                            )
                        }
                        OutlinedTextField(
                            value = input_manufacturer.value,
                            onValueChange = {
                                input_manufacturer.value = it
                            }, enabled = true,
                            modifier = modifier_element,
                            label = { Text(label_manufacturer) },
                            maxLines = 1,
                            colors = inputs_colors
                        )
                        OutlinedTextField(
                            value = input_product_name.value,
                            onValueChange = {
                                input_product_name.value = it
                            }, enabled = true,
                            modifier = modifier_element,
                            label = { Text(label_product_name) },
                            maxLines = 1,
                            colors = inputs_colors
                        )
                        Row(
                            modifier = modifier_element
                                .background(color_white, shape = RoundedCornerShape(10.dp)),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Switch(modifier = Modifier.padding(start = 15.dp),
                                checked = switchV_easily_segreable.value,
                                onCheckedChange = {
                                    switchV_easily_segreable.value = it

                                    if (color_easily_segreable_switch.value == color_yellow) {
                                        color_easily_segreable_switch.value = color_black
                                        easily_Segreable_Text.value = value_to_be_assigned_1
                                    } else{
                                        easily_Segreable_Text.value = value_to_be_assigned_2
                                        color_easily_segreable_switch.value = color_yellow
                                    }
                                },
                                colors = switch_colors
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
                                .background(color_white, shape = RoundedCornerShape(10.dp)),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Switch(modifier = Modifier.padding(start = 15.dp),
                                checked = switchV_required_washing.value,
                                onCheckedChange = {
                                    switchV_required_washing.value = it

                                    if (it){
                                        required_washing.value = value_to_be_assigned_3
                                        color_required_washing_switch.value = color_black
                                    } else {
                                        required_washing.value = value_to_be_assigned_4
                                        color_required_washing_switch.value = color_yellow
                                    }
                                },
                                colors = switch_colors
                            )
                            Text(
                                text = required_washing.value,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .padding(start = 40.dp),
                                color = color_required_washing_switch.value
                            )
                        }
                        Text(
                            text = in_which_containter_to_throw.value,
                            color = color_selected_container.value,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            modifier = modifier_element
                        )
                        if (switchV_easily_segreable.value) {
                            Row(
                                modifier = modifier_element,
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically

                            ) {

                                Icon(imageVector = Icons.Default.Delete,
                                    contentDescription = "",
                                    tint = Color.Blue,
                                    modifier = Modifier
                                        .size(icon1_size.value)
                                        .combinedClickable(
                                            onClick = {
                                                icon1_size.value = icon_size_clicked
                                                icon2_size.value = icon_size_common
                                                icon3_size.value = icon_size_common
                                                icon4_size.value = icon_size_common
                                                icon5_size.value = icon_size_common
                                                in_which_containter_to_throw.value =
                                                    value_to_be_assigned_5
                                                color_selected_container.value = Color.Blue
                                                selected_container.value = "Blue"
                                            }
                                        )
                                        .padding(
                                            start = basket_icon_padding,
                                            end = basket_icon_padding
                                        )
                                )
                                Icon(imageVector = Icons.Default.Delete,
                                    contentDescription = "",
                                    tint = Color.Yellow,
                                    modifier = Modifier
                                        .size(icon2_size.value)
                                        .combinedClickable(
                                            onClick = {
                                                icon1_size.value = icon_size_common
                                                icon2_size.value = icon_size_clicked
                                                icon3_size.value = icon_size_common
                                                icon4_size.value = icon_size_common
                                                icon5_size.value = icon_size_common

                                                in_which_containter_to_throw.value =
                                                    value_to_be_assigned_6

                                                color_selected_container.value = Color.Yellow
                                                selected_container.value = "Yellow"
                                            }
                                        )
                                        .padding(
                                            start = basket_icon_padding,
                                            end = basket_icon_padding
                                        )
                                )
                                Icon(imageVector = Icons.Default.Delete,
                                    contentDescription = "",
                                    tint = Color.Green,
                                    modifier = Modifier
                                        .size(icon3_size.value)
                                        .combinedClickable(
                                            onClick = {
                                                icon1_size.value = icon_size_common
                                                icon2_size.value = icon_size_common
                                                icon3_size.value = icon_size_clicked
                                                icon4_size.value = icon_size_common
                                                icon5_size.value = icon_size_common

                                                in_which_containter_to_throw.value =
                                                    value_to_be_assigned_7

                                                color_selected_container.value = Color.Green
                                                selected_container.value = "Green"
                                            }
                                        )
                                        .padding(
                                            start = basket_icon_padding,
                                            end = basket_icon_padding
                                        )
                                )
                                Icon(imageVector = Icons.Default.Delete,
                                    contentDescription = "",
                                    tint = Color.Black,
                                    modifier = Modifier
                                        .size(icon4_size.value)
                                        .combinedClickable(
                                            onClick = {
                                                icon1_size.value = icon_size_common
                                                icon2_size.value = icon_size_common
                                                icon3_size.value = icon_size_common
                                                icon4_size.value = icon_size_clicked
                                                icon5_size.value = icon_size_common

                                                in_which_containter_to_throw.value =
                                                    value_to_be_assigned_8

                                                color_selected_container.value = Color.Black
                                                selected_container.value = "Black"
                                            }
                                        )
                                        .padding(
                                            start = basket_icon_padding,
                                            end = basket_icon_padding
                                        )
                                )
                                Icon(imageVector = Icons.Default.Delete,
                                    contentDescription = "",
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .size(icon5_size.value)
                                        .combinedClickable(
                                            onClick = {
                                                icon1_size.value = icon_size_common
                                                icon2_size.value = icon_size_common
                                                icon3_size.value = icon_size_common
                                                icon4_size.value = icon_size_common
                                                icon5_size.value = icon_size_clicked

                                                in_which_containter_to_throw.value =
                                                    value_to_be_assigned_9

                                                color_selected_container.value = Color.Red
                                                selected_container.value = "Red"
                                            }
                                        )
                                        .padding(
                                            start = basket_icon_padding,
                                            end = basket_icon_padding
                                        )
                                )
                            }
                        } else {
                            OutlinedTextField(
                                value = input_dumping_description.value,
                                onValueChange = {
                                    input_dumping_description.value = it
                                }, enabled = true,
                                modifier = modifier_element, shape = RoundedCornerShape(20.dp),
                                maxLines = 6,
                                label = {
                                    Text(
                                        text = label_description,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        )
                                },
                                colors = inputs_colors
                            )
                        }
                    }
                }
            }
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


    private suspend fun onConfirm(
        code: String,
        manufacturer: String,
        product_name: String,
        switch_state: Boolean,
        selected_basket: String,
        description: String,
        error_info: MutableState<String>,
        washing: Boolean,
    ){ // switch_state == true -> user selected one of the baskets

        val Evaluate = EvaluateTheNewCode()
        error_info.value = Evaluate.barcodeIsOk(code)

        if(manufacturer == ""){
            error_info.value = "Enter manufacturer"
        }else{
            if(product_name == ""){
                error_info.value = "Enter product name"
            }else{
                if(switch_state && selected_basket ==""){
                    error_info.value = "You must select one of the baskets"
                }else{

                    if (Evaluate.check_if_code_in_registry()) {
                        error_info.value = "Bar Code is already in register"
                    } else {
                        val barcode: Barcode
                        if (switch_state) {
                            val users_opinion = OpinionOnThrowingAway(
                                kind_of_basket = selected_basket,
                                descripton = ""
                            )
                            barcode = Evaluate.getDataFromBarcode(
                                new_opinion = users_opinion,
                                required_washing = washing
                            )!!

                            barcode.manufacturer = manufacturer
                            barcode.product_name = product_name

                            val database =
                                Firebase.database("https://barcodescanner4-cbe9f-default-rtdb.europe-west1.firebasedatabase.app/")
                            val myRef = database.getReference("barcodes").child(RAM_Database.list_of_barcodes.size.toString())

                            myRef.setValue(barcode)
                        } else {
                            val users_opinion = OpinionOnThrowingAway(
                                kind_of_basket = "",
                                descripton = description
                            )
                            barcode = Evaluate.getDataFromBarcode(
                                new_opinion = users_opinion,
                                required_washing = washing
                            )!!


                            barcode.manufacturer = manufacturer
                            barcode.product_name = product_name

                            val database =
                                Firebase.database("https://barcodescanner4-cbe9f-default-rtdb.europe-west1.firebasedatabase.app/")
                            val myRef = database.getReference("barcodes").child(RAM_Database.list_of_barcodes.size.toString())

                            myRef.setValue(barcode)

                        }
                    }
                }
            }
        }
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


