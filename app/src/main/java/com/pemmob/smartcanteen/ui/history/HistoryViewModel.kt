package com.pemmob.smartcanteen.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.smartcanteen.data.model.CartState
import com.pemmob.smartcanteen.data.model.MenuItem
import com.pemmob.smartcanteen.data.model.Order
import com.pemmob.smartcanteen.data.repository.CanteenRepository
import com.pemmob.smartcanteen.data.repository.FakeCanteenRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    private val repository: CanteenRepository = FakeCanteenRepository
) : ViewModel() {

    val cartState: StateFlow<CartState> = repository.cartState

    val historyOrders: StateFlow<List<Order>> = repository.getHistoryOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun reorder(order: Order) {
        for (item in order.items) {
            val menuItem = MenuItem(
                id = item.menuId,
                name = item.menuName,
                description = "",
                price = item.priceAtPurchase,
                stock = 99,
                photoUrl = "",
                category = "General"
            )
            repository.addToCart(menuItem)
        }
    }

    fun dismissCartNotice() {
        repository.dismissCartNotice()
    }
}
