package com.example.ordermyt

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.Profile
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ordermyt.ui.theme.AppBarColor
import com.example.ordermyt.ui.theme.ListItemColor
import com.example.ordermyt.ui.theme.PrimaryButtonColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Integer.parseInt

@Composable
fun HomeScreen(feedbackNavigation: () -> Unit, applicationContext: Context, navigate: () -> Unit) {

    androidx.compose.material.Scaffold(
        topBar = {
            AppBar(navigate = navigate)
        }

    ) {
        var tabSelection by remember {mutableStateOf(1)}
        var tabs = listOf("Profile", "Order", "History")
        Box(modifier = Modifier.padding(it)) {
            Column() {


                TabRow(selectedTabIndex = tabSelection, backgroundColor = AppBarColor,) {
                    tabs.forEachIndexed { i, s ->
                        Tab(onClick = {
                                      tabSelection = i
                        }, text = { Text(s) }, selected = tabSelection == i)
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    when (tabs[tabSelection]) {
                        "Profile" -> {
                            ProfileTab(feedbackNavigation, applicationContext = applicationContext)
                        }

                        "Order" -> {
                            OrderTab()
                        }

                        "History" -> {
                            HistoryTab()

                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderTab() {
    var sf = LocalContext.current.getSharedPreferences("id", Context.MODE_PRIVATE)
    var orderSharedPref = LocalContext.current.getSharedPreferences("ProfileInformation-${sf.getString("id", "")}", Context.MODE_PRIVATE)

    var scaffoldStated = rememberScaffoldState()
    var exposedDropdown by remember {mutableStateOf(false)}
    var dropdownSelection by remember {mutableStateOf("1" )}
    var selected by remember {mutableStateOf(0)}
    var titleState  = orderTypes[selected].title
    var price = orderTypes[selected].price

    var size by remember {mutableStateOf("Medium")}
    var sizePrice = if(size == "Medium") 0f else 3.5f
    var flavour by remember {mutableStateOf("Vanilla")}
    var flavourPrice = when(flavour) {
        "Vanilla" -> 0.0f
        "Chocolate" -> 1.0f
        "Durian" -> 3.0f
        else -> {
            0.0f
        }
    }
    var total = price * parseInt(dropdownSelection) + sizePrice * parseInt(dropdownSelection) + flavourPrice * parseInt(dropdownSelection)
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

                        ChoiceButton(text = "ADD TO ORDER", selected = true, onClick = {
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
                                        put(JSONObject().apply {
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
                                scaffoldState.snackbarHostState.showSnackbar("Item added", duration = SnackbarDuration.Short)


                            }

                        })
                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "+$${sizePrice * parseInt(dropdownSelection)}", fontWeight = FontWeight.Bold)
                        Text(text = "$size Size", fontWeight = FontWeight.Bold)


                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "+$" + flavourPrice * parseInt(dropdownSelection), fontWeight = FontWeight.Bold)
                            Text(text = flavour, fontWeight = FontWeight.Bold)


                    }

            }
        }



    }, scaffoldState = scaffoldState,  sheetPeekHeight = 100.dp, sheetElevation = 10.dp, modifier = Modifier.fillMaxHeight()) {


            Column(modifier = Modifier.fillMaxWidth()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 16.dp)) {
                    items(orderTypes.size) {
                        Card(elevation = 5.dp, backgroundColor = Color.White, modifier = Modifier.size(width = 150.dp, height = 170.dp), onClick = {
                            println("Clicked")

                        }) {
                            Image(painter = painterResource(R.drawable.bubble_tea), contentDescription = "Bubble Tea", modifier = Modifier.clickable {
                                println("????")
                                selected = it

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
                    Text("Ice Cream Flavour", modifier =Modifier.padding(bottom = 20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround,  verticalAlignment = Alignment.CenterVertically) {
                        ChoiceButton(text = "VANILLA", selected = flavour == "Vanilla", onClick = {flavour = "Vanilla"})
                        ChoiceButton(text = "CHOCOLATE", selected =  flavour == "Chocolate", onClick = {flavour = "Chocolate"})
                        ChoiceButton(text = "DURIAN", selected =  flavour == "Durian", onClick = {flavour = "Durian"})
                    }
                }
                val scaffoldState = rememberBottomSheetScaffoldState(           bottomSheetState = remember { BottomSheetState(BottomSheetValue.Collapsed) })





            }

    }
}

@Composable
fun HeaderText(text : String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(vertical = 10.dp))
}

@Composable
fun ProfileTab(feedbackNavigation: () -> Unit, applicationContext: Context) {
    var scaffoldState = rememberScaffoldState()
    androidx.compose.material.Scaffold(scaffoldState = scaffoldState) {
        Box(modifier = Modifier.padding(it)) {
            var scope = rememberCoroutineScope()
            var points by remember {mutableStateOf("0")}
            val sf = LocalContext.current.getSharedPreferences("id", Context.MODE_PRIVATE).getString("id", "")
            val imageSharedPref = applicationContext.getSharedPreferences("ProfileInformation-${sf}", Context.MODE_PRIVATE)
            val orderSharedPref = applicationContext.getSharedPreferences("ProfileInformation-${sf}", Context.MODE_PRIVATE)
            var image by remember {mutableStateOf(imageSharedPref.getString("Image", ""))}
            var favouriteDrink by remember {mutableStateOf(JSONArray())}
            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    if (sf != null) {
                        favouriteDrink = OrderMyTService.getPopular(sf).getJSONArray("popular")
                    }
                }

            }
            LaunchedEffect(Unit) {
                println(image + "WHT THE FUCK")

            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = {


                    if (it != null) {
                        applicationContext.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        image = it.toString()
                        with(imageSharedPref.edit()) {
                            this.putString("Image", it.toString())
                            apply()
                        }
                    }
                }
            )
            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    var res = sf?.let { OrderMyTService.getPoints(it) }
                    println(res)
                    if (res?.has("points")!!) {
                        points = sf?.let { OrderMyTService.getPoints(it).getString("points") }.toString()
                    }
                }

            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Card(elevation = 5.dp, modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), shape = RoundedCornerShape(2.dp )) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        if(Uri.parse(image) != null && image != "") {
                            LaunchedEffect(Unit) {
                                println(Uri.parse(image))
                            }

                            Image(
                                BitmapFactory.decodeStream(applicationContext.contentResolver.openInputStream(Uri.parse(image))).asImageBitmap(), modifier = Modifier
                                    .clip(CircleShape)
                                    .size(100.dp)
                                    .clickable {
                                        scope.launch {
                                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                        }

                                    }, contentDescription = "Profile")
                        } else {
                            Image(
                                painterResource(R.drawable.bubble_tea), modifier = Modifier
                                    .clip(CircleShape)
                                    .size(100.dp)
                                    .clickable {
                                        scope.launch {
                                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                        }

                                    }, contentDescription = "Profile")
                        }

                    }
                }

                HeaderText("Rewards")
                Card(elevation = 5.dp, modifier = Modifier
                    .padding(14.dp)
                    .fillMaxWidth()
                    .height(80.dp), shape = RoundedCornerShape(30.dp), backgroundColor = ListItemColor, ) {
                    Column( verticalArrangement = Arrangement.Center, modifier = Modifier.padding(start = 10.dp)) {
                        HeaderText(text = points + " points")
                    }
                }

                HeaderText("Favourite Drink")
                LazyColumn {
                    items(favouriteDrink.length()) {
                        Card(shape = RoundedCornerShape(40.dp), backgroundColor = ListItemColor, modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp).padding(top = 20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                                Image(painterResource(id = R.drawable.bubble_tea), contentDescription = "Bubble tea", modifier = Modifier.size(80.dp))
                                Column(verticalArrangement = Arrangement.SpaceEvenly) {
                                    Text(text = favouriteDrink[it].toString().split(",")[1])
                                    Text(text = favouriteDrink[it].toString().split(",")[2] + " sized, " + favouriteDrink[it].toString().split(",")[3])




                                }

                                Column() {
                                    ChoiceButton(text = "ORDER", selected = true, fontSize = 10.sp, onClick = {
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
                                                put(JSONObject().apply {
                                                    put("Quantity", favouriteDrink[it].toString().split(",")[0])
                                                    put("Title", favouriteDrink[it].toString().split(",")[1])
                                                    put("Total", calcOrder(favouriteDrink[it].toString().split(",")[1], parseInt(favouriteDrink[it].toString().split(",")[0]), favouriteDrink[it].toString().split(",")[2], favouriteDrink[it].toString().split(",")[3]))
                                                    put("Flavour", favouriteDrink[it].toString().split(",")[3])
                                                    put("Size",favouriteDrink[it].toString().split(",")[2])
                                                })

                                            })

                                        }
                                        println(json)
                                        with(orderSharedPref.edit()) {
                                            putString("Orders", json.toString())
                                            apply()
                                        }
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar("Item has been ordered", duration = SnackbarDuration.Short)
                                        }



                                    })
                                }


                            }
                        }
                    }
                    item {

                        Card(shape = RoundedCornerShape(24.dp), backgroundColor = ListItemColor, modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(top = 20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                                Image(painter = painterResource(R.drawable.bubble_tea), contentDescription = "Feedback" )
                                Text(text = "A need to feedback on our serivce?")
                                ChoiceButton(text = "FEEDBACK", selected = true, onClick = feedbackNavigation, fontSize = 10.sp)
                            }
                        }
                    }

                }


            }
        }
    }

}

@Composable
fun HistoryTab() {
    var sf = LocalContext.current.getSharedPreferences("id", Context.MODE_PRIVATE)
    var scope = rememberCoroutineScope()
    var scafoldState = rememberScaffoldState()
    var orderSharedPref = LocalContext.current.getSharedPreferences("ProfileInformation-${sf.getString("id", "")}", Context.MODE_PRIVATE)
    var ob by remember {mutableStateOf(JSONObject().apply {
        put("history", JSONArray())
    }) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            ob = sf.getString("id", "")?.let { OrderMyTService.getOrders(it) }!!

        }


    }
    androidx.compose.material.Scaffold(scaffoldState = scafoldState) {
        Box(modifier = Modifier.padding(it)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                HeaderText(text = "Recent Orders")
                //calculate  total
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(ob.getJSONArray("history").length()) {
                        Card(shape = RoundedCornerShape(30.dp), backgroundColor = ListItemColor, modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)) {
                            var price: Float? = null

                            for(i in orderTypes) {
                                if(i.title ==  ob.getJSONArray("history").getJSONObject(it).getString("name")) {
                                    price = i.price
                                    break
                                }
                            }



                            var sizePrice = if( ob.getJSONArray("history").getJSONObject(it).getJSONArray("cust")[0]== "Medium") 0f else 3.5f
                            var flavour = ob.getJSONArray("history").getJSONObject(it).getJSONArray("cust")[1]
                            println(flavour)
                            var flavourPrice = when(flavour) {
                                "Vanilla" -> 0.0f
                                "Chocolate" -> 1.0f
                                "Durian" -> 3.0f
                                else -> {
                                    0.0f
                                }
                            }
                            println(flavourPrice)

                            println(price?:0 * parseInt(ob.getJSONArray("history").getJSONObject(it).getString("Qty")) + sizePrice * parseInt(ob.getJSONArray("history").getJSONObject(it).getString("Qty")) + flavourPrice * parseInt(ob.getJSONArray("history").getJSONObject(it).getString("Qty")))

                            println(parseInt(ob.getJSONArray("history").getJSONObject(it).getString("Qty")))
                            var total = (price?:0.0f) * parseInt(ob.getJSONArray("history").getJSONObject(it).getString("Qty")) + sizePrice * parseInt(ob.getJSONArray("history").getJSONObject(it).getString("Qty")) + flavourPrice * parseInt(ob.getJSONArray("history").getJSONObject(it).getString("Qty"))
                            println(price?:0 * parseInt(ob.getJSONArray("history").getJSONObject(it).getString("Qty")) + sizePrice * parseInt(ob.getJSONArray("history").getJSONObject(it).getString("Qty")))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                                Image(painterResource(id = R.drawable.bubble_tea), contentDescription = "Bubble tea", modifier = Modifier.size(80.dp))
                                Column(verticalArrangement = Arrangement.SpaceEvenly) {
                                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(0.9f)) {
                                        Text(text = ob.getJSONArray("history").getJSONObject(it).getString("name"))
                                        Text(text = total.toString() )
                                    }

                                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(0.9f),verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "${ob.getJSONArray("history").getJSONObject(it).getJSONArray("cust")[0]}, ${ob.getJSONArray("history").getJSONObject(it).getJSONArray("cust")[1]}")
                                        ChoiceButton(text = "ORDER AGAIN", selected = true, fontSize = 10.sp, onClick = {

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
                                                        put(JSONObject().apply {
                                                            put("Quantity", ob.getJSONArray("history").getJSONObject(it).getString("Qty"))
                                                            put("Title", ob.getJSONArray("history").getJSONObject(it).getString("name"))
                                                            put("Total", total)
                                                            put("Flavour", ob.getJSONArray("history").getJSONObject(it).getJSONArray("cust")[1])
                                                            put("Size", ob.getJSONArray("history").getJSONObject(it).getJSONArray("cust")[0])
                                                        })

                                                    })

                                                }
                                                println(json)
                                                with(orderSharedPref.edit()) {
                                                    putString("Orders", json.toString())
                                                    apply()
                                                }
                                                scope.launch {
                                                    scafoldState.snackbarHostState.showSnackbar("Item has been ordered", duration = SnackbarDuration.Short)
                                                }



                                            }

                                        })

                                    }

                                }


                            }
                        }
                    }

                }

            }
        }
    }



}

@Composable
fun ChoiceButton(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit = {}, fontSize: TextUnit = 14.sp) {
    Button(onClick = onClick    , colors = if (selected) ButtonDefaults.buttonColors(PrimaryButtonColor) else  ButtonDefaults.buttonColors(Color.White), elevation =ButtonDefaults.elevation(5.dp), modifier = modifier) {
        Text(text = text, fontSize = fontSize)
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ProfileTab(feedbackNavigation = {}, null!!)
}