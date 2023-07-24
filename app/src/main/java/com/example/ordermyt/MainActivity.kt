package com.example.ordermyt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgs
import androidx.navigation.navArgument
import com.example.ordermyt.ui.theme.AppBarColor
import com.example.ordermyt.ui.theme.OrderMyTTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderMyTTheme {
                val permissionsLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = {isGranted -> }
                )
                LaunchedEffect(key1 = Unit ) {
                    permissionsLauncher.launch(android.Manifest.permission.INTERNET)
                    permissionsLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    permissionsLauncher.launch(android.Manifest.permission.ACCESS_NETWORK_STATE)
                }
                // A surface container using the 'background' color from the theme
                val navControllerState = rememberNavController()
                NavHost(navController = navControllerState, startDestination = "login") {
                    composable("login") {
                        LoginScreen({navControllerState.navigate("home")})
                    }
                    composable("home") {
                        HomeScreen({navControllerState.navigate("feedback")}, applicationContext, {navControllerState.navigate("checkout")})
                    }

                    composable("feedback") {
                        FeedbackScreen(this@MainActivity)
                    }

                    composable("checkout") {
                        CheckoutScreen({navControllerState.navigate("home")}, {it -> navControllerState.navigate(it)})
                    }
                    composable("editOrder/{orderIndex}", arguments = listOf(navArgument("orderIndex") {
                        type = NavType.IntType
                    })) { navBackStackEntry ->
                        val orderIndex = navBackStackEntry.arguments?.getInt("orderIndex")
                        orderIndex?.let { EditOrderScreen(it, {navControllerState.navigate("checkout")}) }
                    }
                }
            }
        }
    }
}


@Composable
fun AppBar(navigate: () -> Unit = {}) {
    TopAppBar(backgroundColor = AppBarColor, title = {Text("OrderMyT")}, actions = { IconButton(
        onClick = navigate) {
        Icon(Icons.Outlined.ShoppingCart, contentDescription = " Shopping Cart" )
    }})

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OrderMyTTheme {
        Greeting("Android")
    }
}