package com.pemmob.smartcanteen.ui.merchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.smartcanteen.data.model.MenuItem
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

    private val _menuSearchQuery = MutableStateFlow("")
    val menuSearchQuery: StateFlow<String> = _menuSearchQuery.asStateFlow()

    private val _selectedStockFilter = MutableStateFlow(StockFilterOption.ALL)
    val selectedStockFilter: StateFlow<StockFilterOption> = _selectedStockFilter.asStateFlow()

    private val _isAddMenuDialogOpen = MutableStateFlow(false)
    private val _editingMenuItem = MutableStateFlow<MenuItem?>(null)

    val uiState: StateFlow<MerchantUiState> = combine(
        combine(
            repository.getOrders(),
            repository.getMenuItems(storeId),
            _selectedFilter
        ) { orders, items, filter -> Triple(orders, items, filter) },
        _menuSearchQuery,
        _selectedStockFilter,
        _isAddMenuDialogOpen,
        _editingMenuItem
    ) { (allOrders, menuItems, filter), query, stockFilter, isAddOpen, editingItem ->
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

        // Filtering for Menu & Stock Tab
        val totalCount = menuItems.size
        val availCount = menuItems.count { it.isActive && it.stock > 0 }
        val outCount = menuItems.count { !it.isActive || it.stock <= 0 }

        val searchFiltered = menuItems.filter { item ->
            query.isBlank() ||
                    item.name.contains(query, ignoreCase = true) ||
                    item.category.contains(query, ignoreCase = true)
        }

        val filteredMenu = when (stockFilter) {
            StockFilterOption.ALL -> searchFiltered
            StockFilterOption.AVAILABLE -> searchFiltered.filter { it.isActive && it.stock > 0 }
            StockFilterOption.OUT_OF_STOCK -> searchFiltered.filter { !it.isActive || it.stock <= 0 }
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
            totalRevenueToday = revenue,
            menuSearchQuery = query,
            selectedStockFilter = stockFilter,
            filteredMenuItems = filteredMenu,
            totalMenuCount = totalCount,
            availableMenuCount = availCount,
            outOfStockMenuCount = outCount,
            isAddMenuDialogOpen = isAddOpen,
            editingMenuItem = editingItem
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MerchantUiState(isLoading = true)
    )

    fun onSelectFilter(filter: OrderStatus?) {
        _selectedFilter.value = filter
    }

    fun onMenuSearchQueryChange(query: String) {
        _menuSearchQuery.value = query
    }

    fun onStockFilterSelect(filter: StockFilterOption) {
        _selectedStockFilter.value = filter
    }

    fun openAddMenuDialog() {
        _isAddMenuDialogOpen.value = true
    }

    fun closeAddMenuDialog() {
        _isAddMenuDialogOpen.value = false
    }

    fun openEditMenuDialog(menuItem: MenuItem) {
        _editingMenuItem.value = menuItem
    }

    fun closeEditMenuDialog() {
        _editingMenuItem.value = null
    }

    fun saveNewMenuItem(
        name: String,
        price: Double,
        stock: Int,
        category: String,
        description: String,
        photoUrl: String
    ) {
        viewModelScope.launch {
            val newMenu = MenuItem(
                id = "menu_${System.currentTimeMillis()}",
                storeId = storeId,
                name = name,
                description = description.ifBlank { "Menu lezat khas kantin" },
                price = price,
                stock = stock,
                photoUrl = photoUrl.ifBlank { "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600" },
                category = category.ifBlank { "Lunch" },
                isHot = false,
                isActive = stock > 0
            )
            repository.addMenuItem(newMenu)
            closeAddMenuDialog()
        }
    }

    fun saveUpdatedMenuItem(menuItem: MenuItem) {
        viewModelScope.launch {
            repository.updateMenuItem(menuItem)
            closeEditMenuDialog()
        }
    }

    fun toggleMenuItemActive(menuId: String, isActive: Boolean) {
        viewModelScope.launch {
            repository.toggleMenuItemActive(menuId, isActive)
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
        }
    }

    fun updateStock(menuId: String, newStock: Int) {
        viewModelScope.launch {
            repository.updateStock(menuId, newStock)
            if (newStock > 0) {
                repository.toggleMenuItemActive(menuId, true)
            }
        }
    }
}
