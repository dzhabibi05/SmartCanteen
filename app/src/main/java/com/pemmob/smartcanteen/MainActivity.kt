package com.pemmob.smartcanteen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pemmob.smartcanteen.ui.auth.AuthScreen
import com.pemmob.smartcanteen.ui.menu.MenuCatalogScreen
import com.pemmob.smartcanteen.ui.navigation.Screen
import com.pemmob.smartcanteen.ui.theme.SmartCanteenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCanteenTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Auth.route
                ) {
                    composable(Screen.Auth.route) {
                        AuthScreen(
                            onNavigateToBuyer = {
                                navController.navigate(Screen.MenuCatalog.route) {
                                    popUpTo(Screen.Auth.route) { inclusive = true }
                                }
                            },
                            onNavigateToMerchant = {
                                Toast.makeText(this@MainActivity, "Dashboard Penjual belum dibuat oleh Dyandra", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    composable(Screen.MenuCatalog.route) {
                        MenuCatalogScreen(
                            onCheckoutClick = {
                                Toast.makeText(this@MainActivity, "Checkout Screen belum dibuat oleh Habibi", Toast.LENGTH_SHORT).show()
                            },
                            onBottomNavTabSelected = { tab ->
                                Toast.makeText(this@MainActivity, "Tab: ${tab.label}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}