package com.pemmob.smartcanteen.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.smartcanteen.data.model.CartItem
import com.pemmob.smartcanteen.data.model.CartState
import com.pemmob.smartcanteen.data.model.MenuItem
import com.pemmob.smartcanteen.data.repository.CanteenRepository
import com.pemmob.smartcanteen.data.repository.FakeCanteenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MenuViewModel(
    private val repository: CanteenRepository = FakeCanteenRepository,
    val storeId: String = "store_1"
) : ViewModel() {

    val categories = listOf("All", "Breakfast", "Lunch", "Snacks", "Drinks")

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    val uiState: StateFlow<MenuUiState> = combine(
        repository.getMenuItems(storeId),
        _searchQuery,
        _selectedCategory
    ) { menuList, query, category ->
        val filtered = menuList.filter { item ->
            val matchCategory = category == "All" || item.category.equals(category, ignoreCase = true)
            val matchQuery = query.isBlank() ||
                    item.name.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true)
            matchCategory && matchQuery
        }
        MenuUiState.Success(filtered)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MenuUiState.Loading
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
    }

    fun addToCart(menuItem: MenuItem) {
        val currentItems = _cartState.value.items
        val currentCartItem = currentItems[menuItem.id]
        val currentQty = currentCartItem?.quantity ?: 0

        if (currentQty >= menuItem.stock) {
            _cartState.update { state ->
                state.copy(userNotice = "Stok ${menuItem.name} hanya tersisa ${menuItem.stock}")
            }
            return
        }

        val updatedMap = currentItems.toMutableMap().apply {
            put(menuItem.id, CartItem(menuItem, currentQty + 1))
        }

        _cartState.update { it.copy(items = updatedMap, userNotice = null) }
    }

    fun decreaseQuantity(menuItem: MenuItem) {
        val currentItems = _cartState.value.items
        val currentCartItem = currentItems[menuItem.id] ?: return
        val currentQty = currentCartItem.quantity

        val updatedMap = currentItems.toMutableMap()
        if (currentQty <= 1) {
            updatedMap.remove(menuItem.id)
        } else {
            updatedMap[menuItem.id] = CartItem(menuItem, currentQty - 1)
        }

        _cartState.update { it.copy(items = updatedMap, userNotice = null) }
    }

    fun clearCart() {
        _cartState.value = CartState()
    }

    fun dismissNotice() {
        _cartState.update { it.copy(userNotice = null) }
    }
}
