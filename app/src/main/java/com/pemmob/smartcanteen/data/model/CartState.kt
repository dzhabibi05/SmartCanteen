package com.pemmob.smartcanteen.data.model

data class CartState(
    val items: Map<String, CartItem> = emptyMap(),
    val userNotice: String? = null
) {
    val totalCount: Int get() = items.values.sumOf { it.quantity }
    val totalPrice: Double get() = items.values.sumOf { it.subtotal }
    val isEmpty: Boolean get() = items.isEmpty()
}
