package com.pemmob.smartcanteen.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.smartcanteen.data.model.Order
import com.pemmob.smartcanteen.data.model.OrderStatus
import com.pemmob.smartcanteen.data.repository.CanteenRepository
import com.pemmob.smartcanteen.data.repository.FakeCanteenRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MyOrdersViewModel(
    private val repository: CanteenRepository = FakeCanteenRepository
) : ViewModel() {

    // Mengambil pesanan aktif (Menunggu Konfirmasi, Diproses, Siap Diambil)
    val activeOrders: StateFlow<List<Order>> = repository.getOrders().map { orders ->
        orders.filter { order ->
            order.status != OrderStatus.SELESAI && order.status != OrderStatus.DITOLAK
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}