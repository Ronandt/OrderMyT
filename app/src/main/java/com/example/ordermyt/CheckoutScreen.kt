package com.example.ordermyt

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ordermyt.ui.theme.AppBarColor
import com.example.ordermyt.ui.theme.ListItemColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun CheckoutScreen(navigateBack: ()-> Unit, navigateEdit: (String) -> Unit) {
    var sf = LocalContext.current.getSharedPreferences("id", Context.MODE_PRIVATE)
    var orderSharedPreferences = LocalContext.current.getSharedPreferences("ProfileInformation-${sf.getString("id", "")}", Context.MODE_PRIVATE)
    var items = JSONObject(orderSharedPreferences.getString("Orders", "{}"))
    var total by remember { mutableStateOf(0f)}
    var jsonArr by remember {mutableStateOf(JSONArray())}
    var scope = rememberCoroutineScope()
    var scaffoldState = rememberScaffoldState()
    LaunchedEffect(Unit) {
        println(orderSharedPreferences.getString("Orders", "{}"))
        if(items.has("prefered_order")) {
            jsonArr = items.getJSONArray("prefered_order")
        }
        for(i in (0..jsonArr.length() -1)) {
            total += jsonArr.getJSONObject(i).getInt("Total")
        }

    }



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

            TopAppBar(backgroundColor = AppBarColor, title = { Text("Checkout") }, navigationIcon = { IconButton(
                onClick = navigateBack ) {
                Icon(Icons.Default.ArrowBack , contentDescription = "Go back")
            }},actions = { IconButton(
                onClick = { /*TODO*/ }) {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = " Shopping Cart" )
            }
            })
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                ) {
                LazyColumn(modifier = Modifier
                    .padding(top = 8.dp).heightIn(0.dp, 400.dp)
                    , verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(jsonArr.length()) {
                        androidx.compose.material.Card(
                            shape = RoundedCornerShape(30.dp),
                            backgroundColor = ListItemColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Image(
                                    painterResource(id = R.drawable.bubble_tea),
                                    contentDescription = "Bubble tea",
                                    modifier = Modifier.size(80.dp)
                                )
                                Column(verticalArrangement = Arrangement.SpaceEvenly) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth(0.9f)
                                    ) {
                                        androidx.compose.material.Text(text = jsonArr.getJSONObject(it).getString("Title"))
                                        androidx.compose.material.Text(text = jsonArr.getJSONObject(it).getString("Total"),)
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth(0.9f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        androidx.compose.material.Text(text = jsonArr.getJSONObject(it).getString("Flavour") + ", " + jsonArr.getJSONObject(it).getString("Size"))
                                        ChoiceButton(text = "EDIT", selected = true, fontSize = 10.sp, onClick = {navigateEdit("editOrder/$it")})

                                    }

                                }


                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(top = 60.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Sub-Total", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(text = total.toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween,  modifier = Modifier.fillMaxWidth(), ) {
                        Text(text = "GST", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(text = (total * 0.07f).toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp)

                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween,  modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Total Due:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(text = ((total * 0.07) + total).toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }

                }
                Spacer(modifier = Modifier.weight(1f))

                ChoiceButton(text = "PAY", selected = true, modifier = Modifier
                    .padding(bottom = 14.dp)
                    .align(Alignment.CenterHorizontally)
                    .width(150.dp), onClick = {
                    scope.launch(Dispatchers.IO) {
                        if(items.has("prefered_order")) {
                            OrderMyTService.orderT(items.apply {
                                put("id", sf.getString("id", ""))
                            })

                            with(orderSharedPreferences.edit()) {
                                clear()
                                apply()
                            }
                            withContext(Dispatchers.Main) {
                               jsonArr = JSONArray()
                                total = 0f
                                scaffoldState.snackbarHostState.showSnackbar("Paid!", duration = SnackbarDuration.Short)
                            }



                        } else {
                            withContext(Dispatchers.Main) {
                                scaffoldState.snackbarHostState.showSnackbar("You do not have any orders", duration = SnackbarDuration.Short)
                            }
                        }

                    }
                })

            }

        }
    }
}