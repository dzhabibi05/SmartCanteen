package com.pemmob.smartcanteen

import com.pemmob.smartcanteen.data.model.MenuItem
import com.pemmob.smartcanteen.data.repository.FakeCanteenRepository
import com.pemmob.smartcanteen.ui.menu.MenuViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MenuViewModelTest {

    private lateinit var viewModel: MenuViewModel

    private val testItem = MenuItem(
        id = "test_1",
        storeId = "store_1",
        name = "Nasi Goreng Test",
        description = "Test Description",
        price = 15000.0,
        stock = 2,
        photoUrl = "",
        category = "Lunch"
    )

    @Before
    fun setUp() {
        FakeCanteenRepository.clearCart()
        viewModel = MenuViewModel(repository = FakeCanteenRepository)
    }

    @Test
    fun cart_starts_empty() {
        val cartState = viewModel.cartState.value
        assertTrue(cartState.isEmpty)
        assertEquals(0, cartState.totalCount)
        assertEquals(0.0, cartState.totalPrice, 0.001)
    }

    @Test
    fun addToCart_increments_count_and_updates_price() {
        viewModel.addToCart(testItem)

        val cartState = viewModel.cartState.value
        assertEquals(1, cartState.totalCount)
        assertEquals(15000.0, cartState.totalPrice, 0.001)
        assertEquals(1, cartState.items[testItem.id]?.quantity)
        assertNull(cartState.userNotice)
    }

    @Test
    fun addToCart_validates_maximum_stock() {
        // testItem stock is 2
        viewModel.addToCart(testItem)
        viewModel.addToCart(testItem)

        var cartState = viewModel.cartState.value
        assertEquals(2, cartState.totalCount)
        assertNull(cartState.userNotice)

        // Attempting to add 3rd item should be blocked by stock validation
        viewModel.addToCart(testItem)

        cartState = viewModel.cartState.value
        assertEquals(2, cartState.totalCount)
        assertNotNull(cartState.userNotice)
        assertTrue(cartState.userNotice!!.contains("hanya tersisa 2"))
    }

    @Test
    fun decreaseQuantity_reduces_count_and_removes_when_zero() {
        viewModel.addToCart(testItem)
        viewModel.addToCart(testItem)

        viewModel.decreaseQuantity(testItem)
        var cartState = viewModel.cartState.value
        assertEquals(1, cartState.totalCount)
        assertEquals(15000.0, cartState.totalPrice, 0.001)

        viewModel.decreaseQuantity(testItem)
        cartState = viewModel.cartState.value
        assertEquals(0, cartState.totalCount)
        assertTrue(cartState.isEmpty)
    }

    @Test
    fun clearCart_resets_all_items() {
        viewModel.addToCart(testItem)
        viewModel.clearCart()

        val cartState = viewModel.cartState.value
        assertTrue(cartState.isEmpty)
        assertEquals(0, cartState.totalCount)
    }

    @Test
    fun category_and_search_state_updates_correctly() {
        viewModel.onCategorySelect("Drinks")
        assertEquals("Drinks", viewModel.selectedCategory.value)

        viewModel.onSearchQueryChange("Ayam")
        assertEquals("Ayam", viewModel.searchQuery.value)
    }

}
