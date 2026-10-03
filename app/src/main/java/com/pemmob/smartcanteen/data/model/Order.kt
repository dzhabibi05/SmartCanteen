package com.pemmob.smartcanteen.data.model

data class Order(
    val id: String,
    val buyerId: String,
    val buyerName: String,
    val items: List<OrderItem>,
    val totalPrice: Double,
    val status: OrderStatus = OrderStatus.MENUNGGU_KONFIRMASI,
    val paymentMethod: PaymentMethod,
    val estimatedMinutes: Int = 15,
    val queueNumber: String,
    val createdAt: Long = System.currentTimeMillis()
)