package com.pemmob.smartcanteen.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pemmob.smartcanteen.data.model.CartState
import com.pemmob.smartcanteen.ui.menu.components.CanteenBottomNav
import com.pemmob.smartcanteen.ui.menu.components.CanteenNavTab
import com.pemmob.smartcanteen.ui.menu.components.CartSummaryBar
import com.pemmob.smartcanteen.ui.menu.components.CategoryFilterRow
import com.pemmob.smartcanteen.ui.menu.components.MenuCard
import com.pemmob.smartcanteen.ui.menu.components.SearchHeader
import com.pemmob.smartcanteen.ui.theme.CanteenPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenTextPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenTextSecondary
import com.pemmob.smartcanteen.ui.theme.CanteenWarmBg
import com.pemmob.smartcanteen.ui.theme.SmartCanteenTheme

@Composable
fun MenuCatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuViewModel = viewModel(),
    onCheckoutClick: (CartState) -> Unit = {},
    onBottomNavTabSelected: (CanteenNavTab) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val cartState by viewModel.cartState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(cartState.userNotice) {
        cartState.userNotice?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissNotice()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CanteenWarmBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                CartSummaryBar(
                    totalCount = cartState.totalCount,
                    totalPrice = cartState.totalPrice,
                    onCheckoutClick = { onCheckoutClick(cartState) }
                )
                CanteenBottomNav(
                    selectedTab = CanteenNavTab.HOME,
                    onTabSelected = onBottomNavTabSelected
                )
            }
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is MenuUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CanteenPrimary)
                }
            }

            is MenuUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            is MenuUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item(span = { GridItemSpan(2) }) {
                        SearchHeader(
                            searchQuery = searchQuery,
                            onSearchQueryChange = viewModel::onSearchQueryChange
                        )
                    }

                    item(span = { GridItemSpan(2) }) {
                        CategoryFilterRow(
                            categories = viewModel.categories,
                            selectedCategory = selectedCategory,
                            onCategorySelected = viewModel::onCategorySelect
                        )
                    }

                    if (state.menuList.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 48.dp, horizontal = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.SearchOff,
                                    contentDescription = null,
                                    tint = CanteenTextSecondary,
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Menu tidak ditemukan",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = CanteenTextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Coba ubah kata kunci atau pilih kategori lain",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CanteenTextSecondary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedButton(
                                    onClick = {
                                        viewModel.onSearchQueryChange("")
                                        viewModel.onCategorySelect("All")
                                    }
                                ) {
                                    Text("Reset Filter")
                                }
                            }
                        }
                    } else {
                        items(
                            items = state.menuList,
                            key = { it.id }
                        ) { menuItem ->
                            val currentQty = cartState.items[menuItem.id]?.quantity ?: 0
                            Box(modifier = Modifier.padding(horizontal = 6.dp)) {
                                MenuCard(
                                    item = menuItem,
                                    quantityInCart = currentQty,
                                    onAddToCart = { viewModel.addToCart(menuItem) },
                                    onIncrease = { viewModel.addToCart(menuItem) },
                                    onDecrease = { viewModel.decreaseQuantity(menuItem) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuCatalogScreenPreview() {
    SmartCanteenTheme {
        MenuCatalogScreen()
    }
}
