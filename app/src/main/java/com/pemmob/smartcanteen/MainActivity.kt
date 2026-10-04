package com.pemmob.smartcanteen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.smartcanteen.ui.auth.AuthScreen
import com.pemmob.smartcanteen.ui.checkout.CheckoutScreen
import com.pemmob.smartcanteen.ui.history.HistoryScreen
import com.pemmob.smartcanteen.ui.menu.MenuCatalogScreen
import com.pemmob.smartcanteen.ui.menu.components.CanteenNavTab
import com.pemmob.smartcanteen.ui.merchant.MerchantDashboardScreen
import com.pemmob.smartcanteen.ui.navigation.Screen
import com.pemmob.smartcanteen.ui.orders.MyOrdersScreen
import com.pemmob.smartcanteen.ui.profile.ProfileScreen
import com.pemmob.smartcanteen.ui.theme.SmartCanteenTheme
import com.pemmob.smartcanteen.ui.tracking.OrderTrackingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCanteenTheme {
                val navController = rememberNavController()

                val handleBuyerBottomNav: (CanteenNavTab) -> Unit = { tab ->
                    val route = when (tab) {
                        CanteenNavTab.HOME -> Screen.MenuCatalog.route
                        CanteenNavTab.MY_ORDERS -> Screen.MyOrders.route
                        CanteenNavTab.HISTORY -> Screen.History.route
                        CanteenNavTab.PROFILE -> Screen.Profile.route
                    }
                    navController.navigate(route) {
                        popUpTo(Screen.MenuCatalog.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

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
                                navController.navigate(Screen.MerchantDashboard.route) {
                                    popUpTo(Screen.Auth.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.MenuCatalog.route) {
                        MenuCatalogScreen(
                            onCheckoutClick = {
                                navController.navigate(Screen.Checkout.route)
                            },
                            onBottomNavTabSelected = handleBuyerBottomNav
                        )
                    }

                    composable(Screen.Checkout.route) {
                        CheckoutScreen(
                            onBackClick = { navController.popBackStack() },
                            onOrderSuccess = { orderId ->
                                navController.navigate(Screen.OrderTracking.createRoute(orderId)) {
                                    popUpTo(Screen.MenuCatalog.route) { inclusive = false }
                                }
                            }
                        )
                    }

                    composable(Screen.MyOrders.route) {
                        MyOrdersScreen(
                            onOrderClick = { orderId ->
                                navController.navigate(Screen.OrderTracking.createRoute(orderId))
                            },
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onBottomNavTabSelected = handleBuyerBottomNav
                        )
                    }

                    composable(Screen.History.route) {
                        HistoryScreen(
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onBottomNavTabSelected = handleBuyerBottomNav,
                            onReorderClick = {
                                navController.navigate(Screen.MenuCatalog.route) {
                                    popUpTo(Screen.MenuCatalog.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onLogoutClick = {
                                navController.navigate(Screen.Auth.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onBottomNavTabSelected = handleBuyerBottomNav
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
                                if (!navController.popBackStack()) {
                                    navController.navigate(Screen.MenuCatalog.route) {
                                        popUpTo(Screen.MenuCatalog.route) { inclusive = true }
                                    }
                                }
                            }
                        )
                    }

                    composable(Screen.MerchantDashboard.route) {
                        MerchantDashboardScreen(
                            onNavigateToAuth = {
                                navController.navigate(Screen.Auth.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
