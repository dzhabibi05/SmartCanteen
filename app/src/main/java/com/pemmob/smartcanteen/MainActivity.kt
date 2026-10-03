package com.pemmob.smartcanteen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pemmob.smartcanteen.ui.auth.AuthScreen
import com.pemmob.smartcanteen.ui.menu.MenuCatalogScreen
import com.pemmob.smartcanteen.ui.navigation.Screen
import com.pemmob.smartcanteen.ui.theme.SmartCanteenTheme
import com.pemmob.smartcanteen.ui.tracking.OrderTrackingScreen
import com.pemmob.smartcanteen.ui.checkout.CheckoutScreen

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
                                navController.navigate(Screen.Checkout.route)
                            },
                            onBottomNavTabSelected = { tab ->

                            }
                        )
                    }
                    composable(Screen.Checkout.route) {
                        CheckoutScreen(
                            onBackClick = { navController.popBackStack() },
                            onOrderSuccess = { orderId ->
                                // Pindah ke layar tracking dan bersihkan backstack checkout
                                navController.navigate(Screen.OrderTracking.createRoute(orderId)) {
                                    popUpTo(Screen.MenuCatalog.route) { inclusive = false }
                                }
                            }
                        )
                    }
                    composable(
                        route = Screen.OrderTracking.route,
                        arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                        OrderTrackingScreen(
                            orderId = orderId,
                            onBackClick = {
                                navController.navigate(Screen.MenuCatalog.route) {
                                    popUpTo(Screen.MenuCatalog.route) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}