package com.pemmob.smartcanteen.ui.merchant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.pemmob.smartcanteen.data.model.MenuItem
import com.pemmob.smartcanteen.data.model.Order
import com.pemmob.smartcanteen.data.model.OrderItem
import com.pemmob.smartcanteen.data.model.OrderStatus
import com.pemmob.smartcanteen.data.model.PaymentMethod
import com.pemmob.smartcanteen.ui.menu.components.MerchantBottomNav
import com.pemmob.smartcanteen.ui.menu.components.MerchantNavTab
import com.pemmob.smartcanteen.ui.merchant.components.AddEditMenuDialog
import com.pemmob.smartcanteen.ui.theme.CanteenBadgeHot
import com.pemmob.smartcanteen.ui.theme.CanteenBadgeHotBg
import com.pemmob.smartcanteen.ui.theme.CanteenBadgeLowStock
import com.pemmob.smartcanteen.ui.theme.CanteenBadgeLowStockBg
import com.pemmob.smartcanteen.ui.theme.CanteenBadgeStock
import com.pemmob.smartcanteen.ui.theme.CanteenBadgeStockBg
import com.pemmob.smartcanteen.ui.theme.CanteenBorder
import com.pemmob.smartcanteen.ui.theme.CanteenPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenPrimaryContainer
import com.pemmob.smartcanteen.ui.theme.CanteenSearchBg
import com.pemmob.smartcanteen.ui.theme.CanteenSecondary
import com.pemmob.smartcanteen.ui.theme.CanteenSecondaryContainer
import com.pemmob.smartcanteen.ui.theme.CanteenSurface
import com.pemmob.smartcanteen.ui.theme.CanteenTextPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenTextSecondary
import com.pemmob.smartcanteen.ui.theme.CanteenWarmBg
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MerchantDashboardScreen(
    onNavigateToAuth: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: MerchantViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(MerchantNavTab.ORDERS) }
    var showNotificationDialog by remember { mutableStateOf(false) }

    if (showNotificationDialog) {
        MerchantNotificationDialog(
            onDismiss = { showNotificationDialog = false }
        )
    }

    if (uiState.isAddMenuDialogOpen) {
        AddEditMenuDialog(
            menuItemToEdit = null,
            onDismiss = viewModel::closeAddMenuDialog,
            onSaveNew = viewModel::saveNewMenuItem,
            onSaveUpdated = viewModel::saveUpdatedMenuItem
        )
    }

    uiState.editingMenuItem?.let { menuItemToEdit ->
        AddEditMenuDialog(
            menuItemToEdit = menuItemToEdit,
            onDismiss = viewModel::closeEditMenuDialog,
            onSaveNew = viewModel::saveNewMenuItem,
            onSaveUpdated = viewModel::saveUpdatedMenuItem
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CanteenWarmBg,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                MerchantBottomNav(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        selectedTab = tab
                    }
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            MerchantNavTab.PROFILE -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    MerchantProfileSection(
                        revenue = uiState.totalRevenueToday,
                        onLogout = onNavigateToAuth
                    )
                }
            }

            MerchantNavTab.MENU -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Top Header Info
                    item {
                        MerchantHeaderSection(
                            onNotificationClick = { showNotificationDialog = true }
                        )
                    }

                    // 2. Title Section & Button + Menu Baru
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Kelola Menu & Stok",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp
                                    ),
                                    color = CanteenTextPrimary
                                )
                                Text(
                                    text = "Atur harga, ketersediaan, dan jumlah porsi",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    color = CanteenTextSecondary
                                )
                            }

                            Button(
                                onClick = { viewModel.openAddMenuDialog() },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CanteenSecondary),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Menu Baru",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // 3. Search Bar
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(CanteenSearchBg)
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search icon",
                                    tint = CanteenTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Box(modifier = Modifier.weight(1f)) {
                                    if (uiState.menuSearchQuery.isEmpty()) {
                                        Text(
                                            text = "Cari nama menu...",
                                            style = TextStyle(
                                                fontSize = 13.sp,
                                                color = CanteenTextSecondary.copy(alpha = 0.8f)
                                            )
                                        )
                                    }

                                    BasicTextField(
                                        value = uiState.menuSearchQuery,
                                        onValueChange = viewModel::onMenuSearchQueryChange,
                                        singleLine = true,
                                        textStyle = TextStyle(
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = CanteenTextPrimary
                                        ),
                                        cursorBrush = SolidColor(CanteenSecondary),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                if (uiState.menuSearchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { viewModel.onMenuSearchQueryChange("") },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Hapus pencarian",
                                            tint = CanteenTextSecondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 4. Filter Chips Status
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val filters = listOf(
                                StockFilterOption.ALL to "Semua (${uiState.totalMenuCount})",
                                StockFilterOption.AVAILABLE to "Tersedia (${uiState.availableMenuCount})",
                                StockFilterOption.OUT_OF_STOCK to "Habis (${uiState.outOfStockMenuCount})"
                            )

                            filters.forEach { (option, label) ->
                                val isSelected = uiState.selectedStockFilter == option
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (isSelected) CanteenSecondary else CanteenSearchBg)
                                        .clickable { viewModel.onStockFilterSelect(option) }
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 13.sp
                                        ),
                                        color = if (isSelected) Color.White else CanteenTextSecondary
                                    )
                                }
                            }
                        }
                    }

                    // 5. List Menu Cards
                    if (uiState.filteredMenuItems.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tidak ada menu pada filter ini",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CanteenTextSecondary
                                )
                            }
                        }
                    } else {
                        items(
                            items = uiState.filteredMenuItems,
                            key = { it.id }
                        ) { menuItem ->
                            MerchantMenuItemCard(
                                item = menuItem,
                                onEditClick = { viewModel.openEditMenuDialog(menuItem) },
                                onStockDecrease = {
                                    if (menuItem.stock > 0) {
                                        viewModel.updateStock(menuItem.id, menuItem.stock - 1)
                                    }
                                },
                                onStockIncrease = {
                                    viewModel.updateStock(menuItem.id, menuItem.stock + 1)
                                },
                                onToggleActive = { isActive ->
                                    viewModel.toggleMenuItemActive(menuItem.id, isActive)
                                }
                            )
                        }
                    }
                }
            }

            MerchantNavTab.ORDERS -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Header Top Info
                    item {
                        MerchantHeaderSection(
                            onNotificationClick = { showNotificationDialog = true }
                        )
                    }

                    // 2. Ringkasan Kartu (Pesanan Aktif & Omset)
                    item {
                        MerchantSummaryCardsSection(
                            activeCount = uiState.activeOrderCount,
                            newCount = uiState.newOrderCount,
                            revenue = uiState.totalRevenueToday
                        )
                    }

                    // 3. Filter Chips Status
                    item {
                        MerchantFilterChipsSection(
                            selectedFilter = uiState.selectedFilter,
                            newCount = uiState.newOrderCount,
                            inProgressCount = uiState.inProgressCount,
                            readyCount = uiState.readyCount,
                            completedCount = uiState.completedCount,
                            onSelectFilter = viewModel::onSelectFilter
                        )
                    }

                    // 4. Judul Section Antrean
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Antrean Pesanan Masuk",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = CanteenTextPrimary
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Auto-update",
                                    tint = CanteenSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Auto-update",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = CanteenSecondary
                                )
                            }
                        }
                    }

                    // 5. Daftar Pesanan Masuk
                    if (uiState.filteredOrders.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tidak ada pesanan dalam kategori ini",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CanteenTextSecondary
                                )
                            }
                        }
                    } else {
                        items(
                            items = uiState.filteredOrders,
                            key = { it.id }
                        ) { order ->
                            MerchantOrderCardItem(
                                order = order,
                                menuItems = uiState.menuItems,
                                onAccept = { viewModel.updateOrderStatus(order.id, OrderStatus.DIPROSES) },
                                onReject = { viewModel.updateOrderStatus(order.id, OrderStatus.DITOLAK) },
                                onMarkReady = { viewModel.updateOrderStatus(order.id, OrderStatus.SIAP_DIAMBIL) },
                                onComplete = { viewModel.updateOrderStatus(order.id, OrderStatus.SELESAI) }
                            )
                        }
                    }

                    // 6. Quick Update Stok Section
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Quick Update Stok",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        ),
                                        color = CanteenTextPrimary
                                    )
                                    Text(
                                        text = "Atur stok menu saat jam sibuk istirahat",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                        color = CanteenTextSecondary
                                    )
                                }
                                Text(
                                    text = "Kelola Semua",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CanteenSecondary
                                    ),
                                    modifier = Modifier.clickable {
                                        selectedTab = MerchantNavTab.MENU
                                    }
                                )
                            }

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                items(
                                    items = uiState.menuItems,
                                    key = { it.id }
                                ) { menuItem ->
                                    QuickStockItemCard(
                                        menuItem = menuItem,
                                        onStockDecrease = {
                                            if (menuItem.stock > 0) {
                                                viewModel.updateStock(menuItem.id, menuItem.stock - 1)
                                            }
                                        },
                                        onStockIncrease = {
                                            viewModel.updateStock(menuItem.id, menuItem.stock + 1)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MerchantMenuItemCard(
    item: MenuItem,
    onEditClick: () -> Unit,
    onStockDecrease: () -> Unit,
    onStockIncrease: () -> Unit,
    onToggleActive: (Boolean) -> Unit
) {
    val isAvailable = item.isActive && item.stock > 0
    val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        .format(item.price)
        .replace("Rp", "Rp ")
        .replace(",00", "")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CanteenSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Image Thumbnail
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(CanteenBorder)
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Info (Title, Price, Badge)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = CanteenTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = formattedPrice,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        ),
                        color = CanteenSecondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Status Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isAvailable) Color(0xFFE8F5E9) else CanteenBadgeLowStockBg)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (isAvailable) "• Tersedia" else "• Stok Habis",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = if (isAvailable) Color(0xFF2E7D32) else CanteenBadgeLowStock
                        )
                    }
                }

                // Edit Pencil Icon Top Right
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Menu",
                        tint = CanteenTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stock & Toggle Switch Container
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CanteenSearchBg)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stepper [-] [15] [+] porsi
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Stok:",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = CanteenTextSecondary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(CanteenSurface)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .clickable { onStockDecrease() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Kurangi Stok",
                                tint = CanteenTextPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Text(
                            text = "${item.stock}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = CanteenTextPrimary
                        )

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .clickable { onStockIncrease() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Tambah Stok",
                                tint = CanteenSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Text(
                        text = "porsi",
                        style = MaterialTheme.typography.bodySmall,
                        color = CanteenTextSecondary
                    )
                }

                // Toggle Switch
                Switch(
                    checked = item.isActive && item.stock > 0,
                    onCheckedChange = { isChecked ->
                        onToggleActive(isChecked)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF2E7D32),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = CanteenBorder
                    )
                )
            }
        }
    }
}

// --- Header Top Component ---
@Composable
private fun MerchantHeaderSection(
    onNotificationClick: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Pesanan Masuk ",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = CanteenTextPrimary
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = "• Buka",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifikasi",
                        tint = CanteenSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CanteenSecondary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profil Merchant",
                        tint = CanteenSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Text(
            text = "Kantin SmartCanteen - Stand #04",
            style = MaterialTheme.typography.bodySmall,
            color = CanteenTextSecondary
        )
    }
}

// --- Summary Cards Section ---
@Composable
private fun MerchantSummaryCardsSection(
    activeCount: Int,
    newCount: Int,
    revenue: Double
) {
    val formattedRevenue = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        .format(revenue)
        .replace("Rp", "Rp ")
        .replace(",00", "")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CanteenSecondaryContainer)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CanteenSecondary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = CanteenSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "$activeCount Pesanan",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = CanteenTextPrimary
                    )
                    Text(
                        text = "$newCount Perlu Konfirmasi",
                        style = MaterialTheme.typography.labelSmall,
                        color = CanteenSecondary
                    )
                }
            }
        }

        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CanteenSurface)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CanteenPrimaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = null,
                        tint = CanteenSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = formattedRevenue,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = CanteenTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Omset Hari Ini",
                        style = MaterialTheme.typography.labelSmall,
                        color = CanteenTextSecondary
                    )
                }
            }
        }
    }
}

// --- Filter Chips Section ---
@Composable
private fun MerchantFilterChipsSection(
    selectedFilter: OrderStatus?,
    newCount: Int,
    inProgressCount: Int,
    readyCount: Int,
    completedCount: Int,
    onSelectFilter: (OrderStatus?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        item {
            FilterChipItem(
                label = "Baru ($newCount)",
                isSelected = selectedFilter == OrderStatus.MENUNGGU_KONFIRMASI,
                onClick = { onSelectFilter(OrderStatus.MENUNGGU_KONFIRMASI) }
            )
        }
        item {
            FilterChipItem(
                label = "Diproses ($inProgressCount)",
                isSelected = selectedFilter == OrderStatus.DIPROSES,
                onClick = { onSelectFilter(OrderStatus.DIPROSES) }
            )
        }
        item {
            FilterChipItem(
                label = "Siap ($readyCount)",
                isSelected = selectedFilter == OrderStatus.SIAP_DIAMBIL,
                onClick = { onSelectFilter(OrderStatus.SIAP_DIAMBIL) }
            )
        }
        item {
            FilterChipItem(
                label = "Selesai ($completedCount)",
                isSelected = selectedFilter == OrderStatus.SELESAI,
                onClick = { onSelectFilter(OrderStatus.SELESAI) }
            )
        }
        item {
            FilterChipItem(
                label = "Semua",
                isSelected = selectedFilter == null,
                onClick = { onSelectFilter(null) }
            )
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) CanteenSecondary else CanteenSearchBg)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (isSelected) Color.White else CanteenTextSecondary
        )
    }
}

// --- Merchant Order Card Item ---
@Composable
private fun MerchantOrderCardItem(
    order: Order,
    menuItems: List<MenuItem>,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onMarkReady: () -> Unit,
    onComplete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CanteenSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Order Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Antrean #${order.queueNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = CanteenTextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OrderStatusBadge(status = order.status)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (order.paymentMethod == PaymentMethod.QRIS) Icons.Default.QrCode else Icons.Default.Payments,
                        contentDescription = null,
                        tint = CanteenSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = order.paymentMethod.name,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = CanteenSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Pemesan: ${order.buyerName}",
                style = MaterialTheme.typography.bodySmall,
                color = CanteenTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Items List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                order.items.forEach { item ->
                    val matchedMenu = menuItems.find { it.id == item.menuId }
                    OrderItemRow(orderItem = item, photoUrl = matchedMenu?.photoUrl)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Action Row
            val formattedTotal = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                .format(order.totalPrice)
                .replace("Rp", "Rp ")
                .replace(",00", "")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Pembayaran",
                        style = MaterialTheme.typography.labelSmall,
                        color = CanteenTextSecondary
                    )
                    Text(
                        text = formattedTotal,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = CanteenSecondary
                    )
                }

                when (order.status) {
                    OrderStatus.MENUNGGU_KONFIRMASI -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = onReject,
                                colors = ButtonDefaults.buttonColors(containerColor = CanteenBadgeLowStockBg),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Tolak",
                                    tint = CanteenBadgeLowStock,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tolak", color = CanteenBadgeLowStock, fontSize = 12.sp)
                            }

                            Button(
                                onClick = onAccept,
                                colors = ButtonDefaults.buttonColors(containerColor = CanteenSecondary),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Proses",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Verifikasi & Proses", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    OrderStatus.DIPROSES -> {
                        Button(
                            onClick = onMarkReady,
                            colors = ButtonDefaults.buttonColors(containerColor = CanteenSecondary),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("Tandai Siap Diambil", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    OrderStatus.SIAP_DIAMBIL -> {
                        Button(
                            onClick = onComplete,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("Selesaikan Pesanan (Customer Mengambil)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    OrderStatus.SELESAI, OrderStatus.DITOLAK -> {
                        // Operational completed state indicator
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderStatusBadge(status: OrderStatus) {
    val (label, bgColor, textColor) = when (status) {
        OrderStatus.MENUNGGU_KONFIRMASI -> Triple("Baru Masuk", CanteenPrimaryContainer, CanteenSecondary)
        OrderStatus.DIPROSES -> Triple("Dimasak (8 min)", CanteenSearchBg, CanteenSecondary)
        OrderStatus.SIAP_DIAMBIL -> Triple("Siap Diambil", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        OrderStatus.SELESAI -> Triple("Selesai", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        OrderStatus.DITOLAK -> Triple("Ditolak", CanteenBadgeLowStockBg, CanteenBadgeLowStock)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
private fun OrderItemRow(
    orderItem: OrderItem,
    photoUrl: String?
) {
    val formattedSubtotal = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        .format(orderItem.subtotal)
        .replace("Rp", "Rp ")
        .replace(",00", "")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = orderItem.menuName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CanteenBorder)
            )

            Column {
                Text(
                    text = "${orderItem.qty}x ${orderItem.menuName}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = CanteenTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Text(
            text = formattedSubtotal,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = CanteenTextPrimary
        )
    }
}

// --- Quick Update Stock Card Component ---
@Composable
private fun QuickStockItemCard(
    menuItem: MenuItem,
    onStockDecrease: () -> Unit,
    onStockIncrease: () -> Unit
) {
    val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        .format(menuItem.price)
        .replace("Rp", "Rp ")
        .replace(",00", "")

    Card(
        modifier = Modifier.width(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CanteenSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(menuItem.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = menuItem.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Badge Stock Top Left
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CanteenSurface.copy(alpha = 0.9f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "Sisa ${menuItem.stock}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = CanteenTextPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = menuItem.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = CanteenTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = formattedPrice,
                style = MaterialTheme.typography.labelSmall,
                color = CanteenTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Stepper Row [-] [qty] [+]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CanteenWarmBg)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(CanteenSurface)
                        .clickable { onStockDecrease() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Kurangi Stok",
                        tint = CanteenTextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "${menuItem.stock}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = CanteenTextPrimary
                )

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(CanteenSecondary)
                        .clickable { onStockIncrease() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah Stok",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MerchantProfileSection(
    revenue: Double,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(CanteenSecondary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = null,
                        tint = CanteenSecondary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Kantin Utama - Stand 01",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CanteenTextPrimary
                    )
                    Text(
                        text = "Penjual / Merchant",
                        fontSize = 12.sp,
                        color = CanteenTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Jam Operasional: 07.00 - 16.00 WIB",
                        fontSize = 11.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Statistik Toko Hari Ini", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total Omset Hari Ini", color = CanteenTextSecondary, fontSize = 13.sp)
                    Text(
                        text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(revenue).replace(",00", ""),
                        fontWeight = FontWeight.Bold,
                        color = CanteenSecondary,
                        fontSize = 15.sp
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Surface(
                    onClick = onLogout,
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = "Keluar Toko / Switch Role",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = CanteenTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MerchantNotificationDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = CanteenSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Notifikasi Toko",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = CanteenTextPrimary
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = CanteenTextSecondary
                        )
                    }
                }

                val notifications = listOf(
                    "🔔 Pesanan baru #SC-1042 menantikan konfirmasi pembayaran QRIS.",
                    "⚠️ Stok Kentang Goreng tersisa 4 porsi.",
                    "✅ Pesanan #SC-1039 telah selesai diambil pelanggan.",
                    "🎉 Total omset toko hari ini mencapai Rp 87.000."
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    notifications.forEach { notif ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CanteenWarmBg),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = notif,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = CanteenTextPrimary,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CanteenSecondary)
                ) {
                    Text("Tutup", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
