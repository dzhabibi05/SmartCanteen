package com.pemmob.smartcanteen.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.smartcanteen.data.model.CartState
import com.pemmob.smartcanteen.data.model.Order
import com.pemmob.smartcanteen.data.model.PaymentMethod
import com.pemmob.smartcanteen.data.repository.CanteenRepository
import com.pemmob.smartcanteen.data.repository.FakeCanteenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val repository: CanteenRepository = FakeCanteenRepository
) : ViewModel() {

    // Membaca state keranjang terpusat dari repository
    val cartState: StateFlow<CartState> = repository.cartState

    private val _selectedPaymentMethod = MutableStateFlow(PaymentMethod.QRIS)
    val selectedPaymentMethod: StateFlow<PaymentMethod> = _selectedPaymentMethod.asStateFlow()

    private val _checkoutResult = MutableStateFlow<Result<Order>?>(null)
    val checkoutResult: StateFlow<Result<Order>?> = _checkoutResult.asStateFlow()

    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
    }

    fun submitOrder() {
        viewModelScope.launch {
            val result = repository.createOrder(_selectedPaymentMethod.value)
            _checkoutResult.value = result
        }
    }

    fun resetCheckoutResult() {
        _checkoutResult.value = null
    }
}