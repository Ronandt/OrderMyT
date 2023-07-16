package com.example.ordermyt

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ordermyt.ui.theme.PrimaryButtonColor

@Composable
fun LoginScreen(navigate: () -> Unit) {
    var mobileNumber by remember {mutableStateOf("")}
    Scaffold() {
        Box(Modifier.padding(it)) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth(), contentScale = ContentScale.Fit)
                Column(modifier = Modifier.weight(0.5f).padding(horizontal = 16.dp)) {
                    OutlinedTextField(value = mobileNumber, onValueChange = {mobileNumber = it}, label = {Text("Mobile")}, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = navigate, colors = ButtonDefaults.buttonColors(
                        PrimaryButtonColor), elevation = ButtonDefaults.elevation(10.dp), modifier = Modifier.fillMaxWidth()) {
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