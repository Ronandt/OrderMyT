package com.example.ordermyt

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Float.parseFloat

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditOrderScreen(index: Int, navigateBack: () -> Unit) {
    var sf = LocalContext.current.getSharedPreferences("id", Context.MODE_PRIVATE)
    var orderSharedPref = LocalContext.current.getSharedPreferences("ProfileInformation-${sf.getString("id", "")}", Context.MODE_PRIVATE)
    var item = JSONObject(orderSharedPref.getString("Orders", "{\"prefered_order\": []}")).getJSONArray("prefered_order").getJSONObject(index)


    var scaffoldStated = rememberScaffoldState()
    var exposedDropdown by remember { mutableStateOf(false) }
    var dropdownSelection by remember { mutableStateOf(item.getString("Quantity") ) }

    var titleState  = item.getString("Title")
    var price = orderTypes.find { titleState ==it.title}!!.price

    var size by remember { mutableStateOf(item.getString("Size")) }
    var sizePrice = if(size == "Medium") 0f else 3.5f
    var flavour by remember { mutableStateOf(item.getString("Flavour")) }
    var flavourPrice = when(flavour) {
        "Vanilla" -> 0.0f
        "Chocolate" -> 1.0f
        "Durian" -> 3.0f
        else -> {
            0.0f
        }
    }
    var total = price * Integer.parseInt(dropdownSelection) + sizePrice * Integer.parseInt(
        dropdownSelection
    ) + flavourPrice * Integer.parseInt(dropdownSelection)
    var scope = rememberCoroutineScope()
    // Final position (swipe up))
    var scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(sheetContent =  {
        Box(Modifier.heightIn(min = 100.dp, max = 400.dp)) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(start = 10.dp)
                    .padding(horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(24.dp)) {

                Divider(
                    modifier = Modifier
                        .width(30.dp)
                        .align(Alignment.CenterHorizontally), thickness = 2.dp
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(text = "Total:", fontWeight = FontWeight.Bold)
                        Text(text = (total.toString()), fontWeight = FontWeight.Bold)
                    }

                    ChoiceButton(text = "FINISH EDIT", selected = true, onClick = {
                        scope.launch {



                            var json = JSONObject(orderSharedPref.getString("Orders", "{}")).apply {

                                var jsonArray: JSONArray;
                                if(has("prefered_order")) {
                                    jsonArray = getJSONArray("prefered_order")
                                    println("HAS")
                                } else {
                                    jsonArray = JSONArray()
                                    println("NOPE")

                                }
                                put("prefered_order", jsonArray.apply {
                                    put(index, JSONObject().apply {
                                        put("Quantity", dropdownSelection)
                                        put("Title", titleState)
                                        put("Total", total)
                                        put("Flavour", flavour)
                                        put("Size", size)
                                    })

                                })

                            }
                            println(json)
                            with(orderSharedPref.edit()) {
                                putString("Orders", json.toString())
                                apply()
                            }
                            navigateBack()
                           //Ff scaffoldState.snackbarHostState.showSnackbar("Item edited", duration = SnackbarDuration.Short)


                        }

                    })
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "+$${sizePrice * Integer.parseInt(dropdownSelection)}", fontWeight = FontWeight.Bold)
                    Text(text = "$size Size", fontWeight = FontWeight.Bold)


                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "+$" + flavourPrice * Integer.parseInt(dropdownSelection), fontWeight = FontWeight.Bold)
                    Text(text = flavour, fontWeight = FontWeight.Bold)


                }

            }
        }



    }, scaffoldState = scaffoldState,  sheetPeekHeight = 100.dp, sheetElevation = 10.dp, modifier = Modifier.fillMaxHeight()) {


        Column(modifier = Modifier.fillMaxWidth()) {
            LazyRow(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(top = 16.dp)) {
                items(1) {
                    Card(elevation = 5.dp, backgroundColor = Color.White, modifier = Modifier.size(width = 150.dp, height = 170.dp), onClick = {
                        println("Clicked")

                    }) {
                        Image(painter = painterResource(R.drawable.bubble_tea), contentDescription = "Bubble Tea", modifier = Modifier.clickable {
                            println("????")


                        })

                    }
                }
            }
            Divider()
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)) {
                Text(titleState, modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp), fontSize = 20.sp)
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 50.dp), horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(text = "Quantity")
                    Row(horizontalArrangement = Arrangement.spacedBy(15.dp), modifier = Modifier.clickable {
                        exposedDropdown = !exposedDropdown
                    }) {
                        ExposedDropdownMenuBox(modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(20.dp), expanded = exposedDropdown, onExpandedChange = {exposedDropdown = !exposedDropdown}) {
                            Text(text = dropdownSelection)
                            ExposedDropdownMenu(expanded = exposedDropdown, onDismissRequest = { exposedDropdown = !exposedDropdown}) {

                                (0 until 5).forEach {
                                    DropdownMenuItem(onClick = { dropdownSelection = it.toString(); exposedDropdown = false}, contentPadding = PaddingValues(8.dp)) {
                                        Text(text = it.toString())
                                    }
                                }

                            }

                        }
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    }

                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Size")
                    Row (horizontalArrangement = Arrangement.SpaceEvenly){
                        ChoiceButton(text = "MEDIUM", selected = size == "Medium", onClick = {size = "Medium"})
                        ChoiceButton(text = "LARGE", selected = size == "Large", onClick = {size = "Large"})
                    }

                }
                Text("Ice Cream Flavour", modifier = Modifier.padding(bottom = 20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround,  verticalAlignment = Alignment.CenterVertically) {
                    ChoiceButton(text = "VANILLA", selected = flavour == "Vanilla", onClick = {flavour = "Vanilla"})
                    ChoiceButton(text = "CHOCOLATE", selected =  flavour == "Chocolate", onClick = {flavour = "Chocolate"})
                    ChoiceButton(text = "DURIAN", selected =  flavour == "Durian", onClick = {flavour = "Durian"})
                }
            }
            val scaffoldState = rememberBottomSheetScaffoldState(           bottomSheetState = remember { BottomSheetState(
                BottomSheetValue.Collapsed) })





        }

    }
}