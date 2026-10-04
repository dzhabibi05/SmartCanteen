package com.pemmob.smartcanteen.data.repository

import com.pemmob.smartcanteen.data.model.CartItem
import com.pemmob.smartcanteen.data.model.CartState
import com.pemmob.smartcanteen.data.model.MenuItem
import com.pemmob.smartcanteen.data.model.Order
import com.pemmob.smartcanteen.data.model.OrderItem
import com.pemmob.smartcanteen.data.model.OrderStatus
import com.pemmob.smartcanteen.data.model.PaymentMethod
import com.pemmob.smartcanteen.data.model.User
import com.pemmob.smartcanteen.data.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object FakeCanteenRepository : CanteenRepository {

    // --- State User (Simulasi Autentikasi) ---
    private val defaultUser = User(
        id = "user_mhs_1",
        name = "Yoga",
        email = "yoga@canteen.ac.id",
        role = UserRole.MAHASISWA
    )
    private val _currentUser = MutableStateFlow(defaultUser)
    override val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    override fun switchUserRole(user: User) {
        _currentUser.value = user
    }

    // --- State Menu & Stok ---
    private val initialMenuItems = listOf(
        MenuItem("menu_1", "store_1", "Nasi Goreng", "Nasi yang di goreng pake toping", 15000.0, 15, "https://images.unsplash.com/photo-1512058564366-18510be2db19?w=600", "Lunch", isHot = true),
        MenuItem("menu_2", "store_1", "Mie Goreng", "Mie yang di goreng pake toping", 12000.0, 10, "https://images.unsplash.com/photo-1585032226651-759b368d7246?w=600", "Lunch", isHot = true),
        MenuItem("menu_3", "store_1", "Nasi Ayam Goreng", "Nasi pake lauk ayam goreng krispi", 13000.0, 8, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=600", "Breakfast", isHot = true),
        MenuItem("menu_4", "store_1", "Nasi Ayam Swir", "Nasi pake lauk ayam yang di swir", 15000.0, 5, "https://images.unsplash.com/photo-1562967914-608f82629710?w=600", "Breakfast", isHot = true),
        MenuItem("menu_5", "store_1", "Es Teh Manis", "Teh melati dingin segar dengan manis pas", 4000.0, 25, "https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=600", "Drinks", isHot = false),
        MenuItem("menu_6", "store_1", "Kopi Susu Aren", "Espresso blend dengan susu segar dan gula aren", 10000.0, 12, "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=600", "Drinks", isHot = false),
        MenuItem("menu_7", "store_1", "Kentang Goreng", "French fries renyah dengan taburan bumbu gurih", 8000.0, 4, "https://images.unsplash.com/photo-1576107232684-1279f3908594?w=600", "Snacks", isHot = false),
        MenuItem("menu_8", "store_1", "Roti Bakar Cokelat Keju", "Roti bakar empuk isi meses cokelat dan keju parut", 10000.0, 6, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=600", "Snacks", isHot = false)
    )

    private val _menuItemsFlow = MutableStateFlow(initialMenuItems)

    override fun getMenuItems(storeId: String): Flow<List<MenuItem>> {
        return _menuItemsFlow.map { list -> list.filter { it.storeId == storeId } }
    }

    override suspend fun getMenuItemById(id: String): MenuItem? {
        return _menuItemsFlow.value.find { it.id == id }
    }

    override suspend fun updateStock(menuId: String, newStock: Int) {
        _menuItemsFlow.update { list ->
            list.map { item ->
                if (item.id == menuId) item.copy(stock = newStock.coerceAtLeast(0)) else item
            }
        }
    }

    // --- State Keranjang Belanja ---
    private val _cartState = MutableStateFlow(CartState())
    override val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    override fun addToCart(menuItem: MenuItem) {
        val currentItems = _cartState.value.items
        val currentQty = currentItems[menuItem.id]?.quantity ?: 0
        val currentItemInRepo = _menuItemsFlow.value.find { it.id == menuItem.id } ?: menuItem

        if (currentQty >= currentItemInRepo.stock) {
            _cartState.update { it.copy(userNotice = "Stok ${menuItem.name} hanya tersisa ${currentItemInRepo.stock}") }
            return
        }

        val updated = currentItems.toMutableMap().apply {
            put(menuItem.id, CartItem(menuItem, currentQty + 1))
        }
        _cartState.update { it.copy(items = updated, userNotice = null) }
    }

    override fun decreaseQuantity(menuItem: MenuItem) {
        val currentItems = _cartState.value.items
        val currentQty = currentItems[menuItem.id]?.quantity ?: return
        val updated = currentItems.toMutableMap()
        if (currentQty <= 1) {
            updated.remove(menuItem.id)
        } else {
            updated[menuItem.id] = CartItem(menuItem, currentQty - 1)
        }
        _cartState.update { it.copy(items = updated, userNotice = null) }
    }

    override fun clearCart() {
        _cartState.value = CartState()
    }

    override fun dismissCartNotice() {
        _cartState.update { it.copy(userNotice = null) }
    }

    // --- State Pesanan (Orders) ---
    private val initialOrders = listOf(
        Order(
            id = "SC-1042",
            buyerId = "user_mhs_2",
            buyerName = "Rian Pratama (Meja #12)",
            items = listOf(
                OrderItem("menu_1", "Nasi Goreng Spesial", 15000.0, 2),
                OrderItem("menu_5", "Es Teh Manis", 4000.0, 1)
            ),
            totalPrice = 34000.0,
            status = OrderStatus.MENUNGGU_KONFIRMASI,
            paymentMethod = PaymentMethod.QRIS,
            queueNumber = "SC-1042"
        ),
        Order(
            id = "SC-1041",
            buyerId = "user_mhs_3",
            buyerName = "Siti Aisyah (Bungkus / Takeaway)",
            items = listOf(
                OrderItem("menu_2", "Mie Goreng Spesial", 12000.0, 1),
                OrderItem("menu_6", "Kopi Susu Gula Aren", 8000.0, 1)
            ),
            totalPrice = 20000.0,
            status = OrderStatus.DIPROSES,
            paymentMethod = PaymentMethod.TUNAI,
            queueNumber = "SC-1041"
        ),
        Order(
            id = "SC-1039",
            buyerId = "user_mhs_4",
            buyerName = "Budi Santoso (Loker #04)",
            items = listOf(
                OrderItem("menu_3", "Nasi Ayam Goreng Krispi", 13000.0, 1)
            ),
            totalPrice = 13000.0,
            status = OrderStatus.SIAP_DIAMBIL,
            paymentMethod = PaymentMethod.TUNAI,
            queueNumber = "SC-1039"
        )
    )

    private val _ordersFlow = MutableStateFlow<List<Order>>(initialOrders)
    private var orderCounter = 43

    override fun getOrders(): Flow<List<Order>> = _ordersFlow.asStateFlow()

    override suspend fun getOrderById(orderId: String): Order? {
        return _ordersFlow.value.find { it.id == orderId }
    }

    override suspend fun createOrder(paymentMethod: PaymentMethod): Result<Order> {
        val cart = _cartState.value
        if (cart.isEmpty) {
            return Result.failure(IllegalStateException("Keranjang masih kosong."))
        }

        val menuItems = _menuItemsFlow.value
        for ((menuId, cartItem) in cart.items) {
            val stockAvailable = menuItems.find { it.id == menuId }?.stock ?: 0
            if (cartItem.quantity > stockAvailable) {
                return Result.failure(IllegalStateException("Stok ${cartItem.menuItem.name} tidak mencukupi."))
            }
        }

        // Potong stok produk
        _menuItemsFlow.update { currentList ->
            currentList.map { item ->
                val orderedQty = cart.items[item.id]?.quantity ?: 0
                item.copy(stock = item.stock - orderedQty)
            }
        }

        // Estimasi waktu: 10 menit waktu dasar + (5 menit * jumlah pesanan aktif)
        val activeQueueCount = _ordersFlow.value.count {
            it.status == OrderStatus.MENUNGGU_KONFIRMASI || it.status == OrderStatus.DIPROSES
        }
        val calculatedEta = 10 + (activeQueueCount * 5)

        val newOrder = Order(
            id = "ORD-${System.currentTimeMillis()}",
            buyerId = _currentUser.value.id,
            buyerName = _currentUser.value.name,
            items = cart.items.values.map {
                OrderItem(
                    menuId = it.menuItem.id,
                    menuName = it.menuItem.name,
                    priceAtPurchase = it.menuItem.price,
                    qty = it.quantity
                )
            },
            totalPrice = cart.totalPrice,
            status = OrderStatus.MENUNGGU_KONFIRMASI,
            paymentMethod = paymentMethod,
            estimatedMinutes = calculatedEta,
            queueNumber = "A-${String.format("%03d", orderCounter++)}"
        )

        _ordersFlow.update { listOf(newOrder) + it }
        clearCart()
        return Result.success(newOrder)
    }

    override suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val targetOrder = _ordersFlow.value.find { it.id == orderId } ?: return

        // Jika pesanan ditolak, pulihkan stok menu
        if (newStatus == OrderStatus.DITOLAK && targetOrder.status != OrderStatus.DITOLAK) {
            _menuItemsFlow.update { list ->
                list.map { item ->
                    val returnedQty = targetOrder.items.find { it.menuId == item.id }?.qty ?: 0
                    item.copy(stock = item.stock + returnedQty)
                }
            }
        }

        _ordersFlow.update { list ->
            list.map { if (it.id == orderId) it.copy(status = newStatus) else it }
        }
    }
}