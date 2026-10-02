package com.pemmob.smartcanteen.data.repository

import com.pemmob.smartcanteen.data.model.MenuItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

object FakeCanteenRepository : CanteenRepository {

    private val initialMenuItems = listOf(
        MenuItem(
            id = "menu_1",
            storeId = "store_1",
            name = "Nasi Goreng",
            description = "Nasi yang di goreng pake toping",
            price = 15000.0,
            stock = 15,
            photoUrl = "https://images.unsplash.com/photo-1512058564366-18510be2db19?w=600",
            category = "Lunch",
            isHot = true,
            isActive = true
        ),
        MenuItem(
            id = "menu_2",
            storeId = "store_1",
            name = "Mie Goreng",
            description = "Mie yang di goreng pake toping",
            price = 12000.0,
            stock = 10,
            photoUrl = "https://images.unsplash.com/photo-1585032226651-759b368d7246?w=600",
            category = "Lunch",
            isHot = true,
            isActive = true
        ),
        MenuItem(
            id = "menu_3",
            storeId = "store_1",
            name = "Nasi Ayam Goreng",
            description = "Nasi pake lauk ayam goreng krispi",
            price = 13000.0,
            stock = 8,
            photoUrl = "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=600",
            category = "Breakfast",
            isHot = true,
            isActive = true
        ),
        MenuItem(
            id = "menu_4",
            storeId = "store_1",
            name = "Nasi Ayam Swir",
            description = "Nasi pake lauk ayam yang di swir",
            price = 15000.0,
            stock = 5,
            photoUrl = "https://images.unsplash.com/photo-1562967914-608f82629710?w=600",
            category = "Breakfast",
            isHot = true,
            isActive = true
        ),
        MenuItem(
            id = "menu_5",
            storeId = "store_1",
            name = "Es Teh Manis",
            description = "Teh melati dingin segar dengan manis pas",
            price = 4000.0,
            stock = 25,
            photoUrl = "https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=600",
            category = "Drinks",
            isHot = false,
            isActive = true
        ),
        MenuItem(
            id = "menu_6",
            storeId = "store_1",
            name = "Kopi Susu Aren",
            description = "Espresso blend dengan susu segar dan gula aren",
            price = 10000.0,
            stock = 12,
            photoUrl = "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=600",
            category = "Drinks",
            isHot = false,
            isActive = true
        ),
        MenuItem(
            id = "menu_7",
            storeId = "store_1",
            name = "Kentang Goreng",
            description = "French fries renyah dengan taburan bumbu gurih",
            price = 8000.0,
            stock = 4,
            photoUrl = "https://images.unsplash.com/photo-1576107232684-1279f3908594?w=600",
            category = "Snacks",
            isHot = false,
            isActive = true
        ),
        MenuItem(
            id = "menu_8",
            storeId = "store_1",
            name = "Roti Bakar Cokelat Keju",
            description = "Roti bakar empuk isi meses cokelat dan keju parut",
            price = 10000.0,
            stock = 6,
            photoUrl = "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=600",
            category = "Snacks",
            isHot = false,
            isActive = true
        )
    )

    private val _menuItemsFlow = MutableStateFlow(initialMenuItems)

    override fun getMenuItems(storeId: String): Flow<List<MenuItem>> {
        return _menuItemsFlow.map { list ->
            list.filter { it.storeId == storeId }
        }
    }

    override suspend fun getMenuItemById(id: String): MenuItem? {
        return _menuItemsFlow.value.find { it.id == id }
    }

    override suspend fun updateStock(menuId: String, newStock: Int) {
        val updated = _menuItemsFlow.value.map { item ->
            if (item.id == menuId) item.copy(stock = newStock.coerceAtLeast(0)) else item
        }
        _menuItemsFlow.value = updated
    }
}
