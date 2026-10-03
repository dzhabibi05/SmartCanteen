package com.pemmob.smartcanteen.ui.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.smartcanteen.data.model.Order
import com.pemmob.smartcanteen.data.repository.CanteenRepository
import com.pemmob.smartcanteen.data.repository.FakeCanteenRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TrackingViewModel(
    private val repository: CanteenRepository = FakeCanteenRepository
) : ViewModel() {

    fun observeOrder(orderId: String): StateFlow<Order?> {
        return repository.getOrders().map { orders ->
            orders.find { it.id == orderId }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }
}