package com.pemmob.smartcanteen.ui.merchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.smartcanteen.data.model.OrderStatus
import com.pemmob.smartcanteen.data.repository.CanteenRepository
import com.pemmob.smartcanteen.data.repository.FakeCanteenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MerchantViewModel(
    private val repository: CanteenRepository = FakeCanteenRepository,
    val storeId: String = "store_1"
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow<OrderStatus?>(OrderStatus.MENUNGGU_KONFIRMASI)
    val selectedFilter: StateFlow<OrderStatus?> = _selectedFilter.asStateFlow()

    val uiState: StateFlow<MerchantUiState> = combine(
        repository.getOrders(),
        repository.getMenuItems(storeId),
        _selectedFilter
    ) { allOrders, menuItems, filter ->
        val activeCount = allOrders.count {
            it.status == OrderStatus.MENUNGGU_KONFIRMASI ||
                    it.status == OrderStatus.DIPROSES ||
                    it.status == OrderStatus.SIAP_DIAMBIL
        }
        val newCount = allOrders.count { it.status == OrderStatus.MENUNGGU_KONFIRMASI }
        val inProgressCount = allOrders.count { it.status == OrderStatus.DIPROSES }
        val readyCount = allOrders.count { it.status == OrderStatus.SIAP_DIAMBIL }
        val completedCount = allOrders.count { it.status == OrderStatus.SELESAI }

        val revenue = allOrders
            .filter { it.status != OrderStatus.DITOLAK }
            .sumOf { it.totalPrice }

        val filteredOrders = if (filter == null) {
            allOrders
        } else {
            allOrders.filter { it.status == filter }
        }

        MerchantUiState(
            isLoading = false,
            allOrders = allOrders,
            filteredOrders = filteredOrders,
            menuItems = menuItems,
            selectedFilter = filter,
            activeOrderCount = activeCount,
            newOrderCount = newCount,
            inProgressCount = inProgressCount,
            readyCount = readyCount,
            completedCount = completedCount,
            totalRevenueToday = revenue
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MerchantUiState(isLoading = true)
    )

    fun onSelectFilter(filter: OrderStatus?) {
        _selectedFilter.value = filter
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
        }
    }

    fun updateStock(menuId: String, newStock: Int) {
        viewModelScope.launch {
            repository.updateStock(menuId, newStock)
        }
    }
}
