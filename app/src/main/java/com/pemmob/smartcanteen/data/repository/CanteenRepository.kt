package com.pemmob.smartcanteen.data.repository

import com.pemmob.smartcanteen.data.model.MenuItem
import kotlinx.coroutines.flow.Flow

interface CanteenRepository {
    fun getMenuItems(storeId: String = "store_1"): Flow<List<MenuItem>>
    suspend fun getMenuItemById(id: String): MenuItem?
    suspend fun updateStock(menuId: String, newStock: Int)
}
