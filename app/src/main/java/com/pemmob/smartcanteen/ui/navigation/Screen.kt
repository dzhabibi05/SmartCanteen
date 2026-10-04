package com.pemmob.smartcanteen.ui.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object MenuCatalog : Screen("menu_catalog")
    data object Checkout : Screen("checkout")
    data object MyOrders : Screen("my_orders")
    data object History : Screen("history")
    data object Profile : Screen("profile")
    data object OrderTracking : Screen("order_tracking/{orderId}") {
        fun createRoute(orderId: String) = "order_tracking/$orderId"
    }
    data object MerchantDashboard : Screen("merchant_dashboard")
}