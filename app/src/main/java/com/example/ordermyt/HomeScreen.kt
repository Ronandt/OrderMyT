package com.example.ordermyt

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.Profile
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
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ordermyt.ui.theme.AppBarColor
import com.example.ordermyt.ui.theme.ListItemColor
import com.example.ordermyt.ui.theme.PrimaryButtonColor
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(feedbackNavigation: () -> Unit, applicationContext: Context) {
    androidx.compose.material.Scaffold(
        topBar = {
            AppBar()
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
    var exposedDropdown by remember {mutableStateOf(false)}
    var dropdownSelection by remember {mutableStateOf("1" )}

     // Final position (swipe up))
    var scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(sheetContent =  {
        Box(Modifier.heightIn(min = 100.dp, max = 400.dp)) {
            LazyColumn(Modifier.height(400.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                item {

                    Divider(
                        modifier = Modifier
                            .width(30.dp)
                            .align(Alignment.TopCenter), thickness = 2.dp
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Total:")
                        ChoiceButton(text = "ADD TO ORDER", selected = true)
                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {

                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {

                    }
                }
            }
        }



    }, scaffoldState = scaffoldState,  sheetPeekHeight = 100.dp, sheetElevation = 10.dp, modifier = Modifier.fillMaxHeight()) {
        Box() {
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 16.dp)) {
                    items(5) {
                        Card(elevation = 5.dp, backgroundColor = Color.White, modifier = Modifier.size(width = 150.dp, height = 170.dp)) {
                            Image(painter = painterResource(R.drawable.bubble_tea), contentDescription = "Bubble Tea", modifier = Modifier.clickable {


                            })

                        }
                    }
                }
                Divider()
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)) {
                    Text("Bubble Milk Tea with Ice Cream!", modifier = Modifier
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
                            ChoiceButton(text = "MEDIUM", selected = true)
                            ChoiceButton(text = "LARGE", selected = false)
                        }

                    }
                    Text("Ice Cream Flavour", modifier =Modifier.padding(bottom = 20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround,  verticalAlignment = Alignment.CenterVertically) {
                        ChoiceButton(text = "VANILIA", selected = true)
                        ChoiceButton(text = "CHOCOLATE", selected = false)
                        ChoiceButton(text = "DURIAN", selected = false)
                    }
                }
                val scaffoldState = rememberBottomSheetScaffoldState(           bottomSheetState = remember { BottomSheetState(BottomSheetValue.Collapsed) })





            }
        }
    }
}

@Composable
fun HeaderText(text : String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(vertical = 10.dp))
}

@Composable
fun ProfileTab(feedbackNavigation: () -> Unit, applicationContext: Context) {
    var scope = rememberCoroutineScope()
    val imageSharedPref = applicationContext.getSharedPreferences("ProfileInformation", Context.MODE_PRIVATE)
    var image by remember {mutableStateOf(imageSharedPref.getString("Image", ""))}
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            with(imageSharedPref.edit()) {
                this.putString("Image", it.toString())
                apply()
            }

            if (it != null) {
                applicationContext.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                image = it.toString()
            }
        }
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Card(elevation = 5.dp, modifier = Modifier
            .fillMaxWidth()
            .height(120.dp), shape = RoundedCornerShape(40.dp )) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                if(image != "") {
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
                HeaderText(text = "3300 points")
            }
        }

        HeaderText("Favourite Drink")
        Card(shape = RoundedCornerShape(40.dp), backgroundColor = ListItemColor, modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                Image(painterResource(id = R.drawable.bubble_tea), contentDescription = "Bubble tea", modifier = Modifier.size(80.dp))
                Column(verticalArrangement = Arrangement.SpaceEvenly) {
                        Text(text = "Bubble milk tea with ice cream")
                        Text(text = "Medium sized, Vanilla")




                }

                Column() {
                    ChoiceButton(text = "ORDER", selected = true)
                }


            }
        }

        Card(shape = RoundedCornerShape(24.dp), backgroundColor = ListItemColor, modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                Image(painter = painterResource(R.drawable.bubble_tea), contentDescription = "Feedback" )
                Text(text = "A need to feedback on our serivce?")
                ChoiceButton(text = "FEEDBACK", selected = true, onClick = feedbackNavigation)
            }
        }
    }
}

@Composable
fun HistoryTab() {
    Column(modifier = Modifier.fillMaxWidth()) {
        HeaderText(text = "Recent Orders")

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(3) {
                    Card(shape = RoundedCornerShape(40.dp), backgroundColor = ListItemColor, modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                            Image(painterResource(id = R.drawable.bubble_tea), contentDescription = "Bubble tea", modifier = Modifier.size(80.dp))
                            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(0.9f)) {
                                    Text(text = "Bubble milk tea with ice cream")
                                    Text(text = "$7.6", )
                                }

                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(0.9f),verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "Large sized, vanila")
                                 ChoiceButton(text = "ORDER AGAIN", selected = true)

                                }

                            }


                        }
                    }
                }

            }

    }

}

@Composable
fun ChoiceButton(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(onClick = onClick    , colors = if (selected) ButtonDefaults.buttonColors(PrimaryButtonColor) else  ButtonDefaults.buttonColors(Color.White), elevation =ButtonDefaults.elevation(5.dp), modifier = modifier) {
        Text(text = text, fontSize = 14.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ProfileTab(feedbackNavigation = {}, null!!)
}