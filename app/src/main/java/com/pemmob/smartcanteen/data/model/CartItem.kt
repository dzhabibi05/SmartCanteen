package com.pemmob.smartcanteen.data.model

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int
) {
    val subtotal: Double get() = menuItem.price * quantity
}
