package com.pemmob.smartcanteen.ui.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.smartcanteen.data.model.Order
import com.pemmob.smartcanteen.data.model.OrderStatus
import com.pemmob.smartcanteen.data.repository.CanteenRepository
import com.pemmob.smartcanteen.data.repository.FakeCanteenRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrackingViewModel(
    private val repository: CanteenRepository = FakeCanteenRepository
) : ViewModel() {

    fun observeOrder(orderId: String): StateFlow<Order?> {
        // Mulai simulasi perubahan status secara otomatis
        startStatusSimulation(orderId)

        return repository.getOrders().map { orders ->
            orders.find { it.id == orderId }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }

    private fun startStatusSimulation(orderId: String) {
        viewModelScope.launch {
            val statusFlow = listOf(
                OrderStatus.MENUNGGU_KONFIRMASI,
                OrderStatus.DIPROSES,
                OrderStatus.SIAP_DIAMBIL,
                OrderStatus.SELESAI
            )

            for (status in statusFlow) {
                // Memanggil suspend function updateOrderStatus dari repository
                repository.updateOrderStatus(orderId, status)
                delay(3000) // Tunggu 10 detik sebelum lanjut ke status berikutnya
            }
        }
    }
}