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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import androidx.compose.foundation.layout.navigationBarsPadding
import com.pemmob.smartcanteen.data.model.MenuItem
import com.pemmob.smartcanteen.data.model.Order
import com.pemmob.smartcanteen.data.model.OrderItem
import com.pemmob.smartcanteen.data.model.OrderStatus
import com.pemmob.smartcanteen.data.model.PaymentMethod
import com.pemmob.smartcanteen.ui.menu.components.MerchantBottomNav
import com.pemmob.smartcanteen.ui.menu.components.MerchantNavTab
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
                        if (tab == MerchantNavTab.PROFILE) {
                            onNavigateToAuth()
                        } else {
                            selectedTab = tab
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- 1. Header Top Info ---
            item {
                MerchantHeaderSection()
            }

            // --- 2. Ringkasan Kartu (Pesanan Aktif & Omset) ---
            item {
                MerchantSummaryCardsSection(
                    activeCount = uiState.activeOrderCount,
                    newCount = uiState.newOrderCount,
                    revenue = uiState.totalRevenueToday
                )
            }

            // --- 3. Filter Chips Status ---
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

            // --- 4. Judul Section Antrean ---
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

            // --- 5. Daftar Pesanan Masuk ---
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

            // --- 6. Quick Update Stok Section ---
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
                            )
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

// --- Header Top Component ---
@Composable
private fun MerchantHeaderSection() {
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
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "• Buka",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CanteenSurface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi",
                            tint = CanteenTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(CanteenPrimary)
                            .align(Alignment.TopEnd)
                    )
                }

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CanteenSecondaryContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profil",
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Kantin Bu Sri",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                ),
                color = CanteenTextPrimary
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE8F5E9))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "● Terima Pesanan",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

// --- Summary Cards Component ---
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
        // Card 1: Pesanan Aktif
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CanteenSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pesanan Aktif",
                        style = MaterialTheme.typography.labelMedium,
                        color = CanteenTextSecondary
                    )
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = CanteenSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$activeCount",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = CanteenTextPrimary
                    )
                    if (newCount > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "+$newCount baru",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Card 2: Omset Hari Ini
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CanteenSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Omset Hari Ini",
                        style = MaterialTheme.typography.labelMedium,
                        color = CanteenTextSecondary
                    )
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = null,
                        tint = CanteenSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formattedRevenue,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = CanteenTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// --- Filter Chips Row Component ---
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
        contentPadding = PaddingValues(vertical = 2.dp)
    ) {
        item {
            FilterChipItem(
                label = "Pesanan Baru ($newCount)",
                isSelected = selectedFilter == OrderStatus.MENUNGGU_KONFIRMASI,
                onClick = { onSelectFilter(OrderStatus.MENUNGGU_KONFIRMASI) }
            )
        }
        item {
            FilterChipItem(
                label = "Sedang Diproses ($inProgressCount)",
                isSelected = selectedFilter == OrderStatus.DIPROSES,
                onClick = { onSelectFilter(OrderStatus.DIPROSES) }
            )
        }
        item {
            FilterChipItem(
                label = "Siap Diambil ($readyCount)",
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
            .background(if (isSelected) CanteenSecondary else CanteenSurface)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp
            ),
            color = if (isSelected) Color.White else CanteenTextPrimary
        )
    }
}

// --- Order Card Component ---
@Composable
private fun MerchantOrderCardItem(
    order: Order,
    menuItems: List<MenuItem>,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onMarkReady: () -> Unit,
    onComplete: () -> Unit
) {
    val formattedTotal = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        .format(order.totalPrice)
        .replace("Rp", "Rp ")
        .replace(",00", "")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CanteenSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Row: Queue #, Time, Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "#${order.queueNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        ),
                        color = CanteenTextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(CanteenSearchBg)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "🕒 12:20 PM",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                color = CanteenTextSecondary
                            )
                        )
                    }
                }

                OrderStatusBadge(status = order.status)
            }

            // Buyer Name Info
            Text(
                text = "Pemesanan: ${order.buyerName}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = CanteenTextPrimary
            )

            // Payment Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CanteenSearchBg)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (order.paymentMethod == PaymentMethod.QRIS) Icons.Default.QrCode else Icons.Default.Payments,
                            contentDescription = null,
                            tint = CanteenSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text(
                                text = if (order.paymentMethod == PaymentMethod.QRIS) "QRIS SmartCanteen" else "Tunai di Kasir (Lunas)",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = CanteenTextPrimary
                            )
                            if (order.paymentMethod == PaymentMethod.QRIS) {
                                Text(
                                    text = "Menunggu verifikasi penjual",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = CanteenTextSecondary
                                )
                            }
                        }
                    }

                    Text(
                        text = formattedTotal,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = CanteenSecondary
                    )
                }
            }

            // Items List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                order.items.forEach { item ->
                    val matchedMenu = menuItems.find { it.id == item.menuId }
                    OrderItemRow(orderItem = item, photoUrl = matchedMenu?.photoUrl)
                }
            }

            // Action Buttons
            when (order.status) {
                OrderStatus.MENUNGGU_KONFIRMASI -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onReject,
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CanteenBadgeLowStockBg,
                                contentColor = CanteenBadgeLowStock
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tolak", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onAccept,
                            modifier = Modifier.weight(1.8f).height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CanteenSecondary)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Konfirmasi & Proses", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                OrderStatus.DIPROSES -> {
                    Button(
                        onClick = onMarkReady,
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CanteenPrimary)
                    ) {
                        Text("🍱 Tandai Siap Diambil", fontWeight = FontWeight.Bold)
                    }
                }

                OrderStatus.SIAP_DIAMBIL -> {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
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
