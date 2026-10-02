package com.pemmob.smartcanteen.data.repository

import com.pemmob.smartcanteen.data.model.CartState
import com.pemmob.smartcanteen.data.model.MenuItem
import com.pemmob.smartcanteen.data.model.Order
import com.pemmob.smartcanteen.data.model.OrderStatus
import com.pemmob.smartcanteen.data.model.PaymentMethod
import com.pemmob.smartcanteen.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CanteenRepository {
    // Menu & Stok
    fun getMenuItems(storeId: String = "store_1"): Flow<List<MenuItem>>
    suspend fun getMenuItemById(id: String): MenuItem?
    suspend fun updateStock(menuId: String, newStock: Int)

    // User & Autentikasi Simulasi
    val currentUser: StateFlow<User>
    fun switchUserRole(user: User)

    // Keranjang Belanja (Single Source of Truth)
    val cartState: StateFlow<CartState>
    fun addToCart(menuItem: MenuItem)
    fun decreaseQuantity(menuItem: MenuItem)
    fun clearCart()
    fun dismissCartNotice()

    // Pesanan (Orders)
    fun getOrders(): Flow<List<Order>>
    suspend fun getOrderById(orderId: String): Order?
    suspend fun createOrder(paymentMethod: PaymentMethod): Result<Order>
    suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus)
}