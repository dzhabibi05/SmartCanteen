package com.pemmob.smartcanteen.data.model

data class OrderItem(
    val menuId: String,
    val menuName: String,
    val priceAtPurchase: Double,
    val qty: Int
) {
    val subtotal: Double get() = priceAtPurchase * qty
}