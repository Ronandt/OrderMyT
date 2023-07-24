package com.example.ordermyt

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ordermyt.ui.theme.PrimaryButtonColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.regex.Pattern

@Composable
fun LoginScreen(navigate: () -> Unit) {
    var mobileNumber by remember {mutableStateOf("")}
    var scope = rememberCoroutineScope()
    var scaffoldState = rememberScaffoldState()
    var error =  !Pattern.compile("^\\+65(6|8|9)\\d{7}$").matcher(mobileNumber).matches()
    var typed by remember{mutableStateOf(false)}
    var sf = LocalContext.current.getSharedPreferences("id", Context.MODE_PRIVATE)
    Scaffold(scaffoldState = scaffoldState) {
        Box(Modifier.padding(it)) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth(), contentScale = ContentScale.Fit)
                Column(modifier = Modifier
                    .weight(0.5f)
                    .padding(horizontal = 16.dp)) {
                    OutlinedTextField(value = mobileNumber, onValueChange = {mobileNumber = it; typed = true}, label = {Text("Mobile")}, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number), isError =  error && typed)
                    if(error && typed) {
                        Text("This has to be a mobile number!", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(enabled = !error && mobileNumber.isNotEmpty(), onClick = {
                        scope.launch(Dispatchers.IO) {
                            try {
                                var response = OrderMyTService.userLogin(JSONObject().apply {
                                    put("mobile", mobileNumber)
                                }).toString()
                                with(sf.edit()) {
                                    putString("id", JSONObject(response).getJSONObject("result").getString("accountid"))
                                    apply()
                                }

                                println(response)
                                withContext(Dispatchers.Main) {

                                    navigate()
                                }
                            } catch(e: Exception) {
                                scaffoldState.snackbarHostState.showSnackbar("There was a problem connecting to the server")
                            }




                        }


                    } , colors = ButtonDefaults.buttonColors(
                        PrimaryButtonColor), elevation = ButtonDefaults.elevation(10.dp), modifier = Modifier.fillMaxWidth(),) {
                        Text("SUBMIT", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(null!!)
}