package com.pemmob.smartcanteen.ui.merchant

import com.pemmob.smartcanteen.data.model.MenuItem
import com.pemmob.smartcanteen.data.model.Order
import com.pemmob.smartcanteen.data.model.OrderStatus

data class MerchantUiState(
    val isLoading: Boolean = false,
    val allOrders: List<Order> = emptyList(),
    val filteredOrders: List<Order> = emptyList(),
    val menuItems: List<MenuItem> = emptyList(),
    val selectedFilter: OrderStatus? = OrderStatus.MENUNGGU_KONFIRMASI,
    val activeOrderCount: Int = 0,
    val newOrderCount: Int = 0,
    val inProgressCount: Int = 0,
    val readyCount: Int = 0,
    val completedCount: Int = 0,
    val totalRevenueToday: Double = 0.0
)
