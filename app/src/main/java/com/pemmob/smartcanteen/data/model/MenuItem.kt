package com.pemmob.smartcanteen.data.model

data class MenuItem(
    val id: String,
    val storeId: String = "store_1",
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val photoUrl: String,
    val category: String,
    val isHot: Boolean = false,
    val isActive: Boolean = true
) {
    val isAvailable: Boolean get() = isActive && stock > 0
}
