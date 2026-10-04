package com.pemmob.smartcanteen.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pemmob.smartcanteen.data.model.Order
import com.pemmob.smartcanteen.data.model.OrderStatus
import com.pemmob.smartcanteen.ui.menu.components.CanteenBottomNav
import com.pemmob.smartcanteen.ui.menu.components.CanteenNavTab
import com.pemmob.smartcanteen.ui.theme.CanteenPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenTextSecondary
import com.pemmob.smartcanteen.ui.theme.CanteenWarmBg
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(
    onOrderClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onBottomNavTabSelected: (CanteenNavTab) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyOrdersViewModel = viewModel()
) {
    val activeOrders by viewModel.activeOrders.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CanteenWarmBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Orders",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CanteenWarmBg)
            )
        },
        bottomBar = {
            // Pemanggilan komponen reusable CanteenBottomNavigationBar dari Home Screen
            Box(
                modifier = Modifier.navigationBarsPadding()
            ){
                CanteenBottomNav(
                    selectedTab = CanteenNavTab.MY_ORDERS,
                    onTabSelected = onBottomNavTabSelected
                )
            }

        }
    ) { innerPadding ->
        if (activeOrders.isEmpty()) {
            EmptyOrdersView(modifier = Modifier.padding(innerPadding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                items(activeOrders, key = { it.id }) { order ->
                    ActiveOrderCard(
                        order = order,
                        onClick = { onOrderClick(order.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun ActiveOrderCard(
    order: Order,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header Card: Nomor Order & Badge Status Dynamic
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #${order.queueNumber}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Estimasi: ± ${order.estimatedMinutes} menit",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                // Status Badge (Orange / Hijau)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = order.status.containerColor
                ) {
                    Text(
                        text = getStatusLabel(order.status),
                        color = order.status.statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(14.dp))

            // Ringkasan Makanan Pertama & Qty
            val firstItem = order.items.firstOrNull()
            if (firstItem != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF6EDE4)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SoupKitchen,
                            contentDescription = null,
                            tint = Color(0xFF8D6E63)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = firstItem.menuName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        val extraItemsText = if (order.items.size > 1) {
                            "+ ${order.items.size - 1} menu lainnya"
                        } else {
                            "Qty: ${firstItem.qty}"
                        }
                        Text(
                            text = extraItemsText,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Lihat Detail",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            // Total Pembayaran
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Pesanan",
                    fontSize = 13.sp,
                    color = CanteenTextSecondary
                )
                Text(
                    text = formatRupiah(order.totalPrice),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = CanteenPrimary
                )
            }
        }
    }
}

@Composable
private fun EmptyOrdersView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFFFF3E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = Color(0xFFF57C00),
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Belum Ada Pesanan Aktif",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Pesanan yang sedang diproses akan muncul di sini",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}

private fun getStatusLabel(status: OrderStatus): String {
    return when (status) {
        OrderStatus.MENUNGGU_KONFIRMASI -> "Menunggu"
        OrderStatus.DIPROSES -> "Diproses"
        OrderStatus.SIAP_DIAMBIL -> "Siap Diambil"
        OrderStatus.SELESAI -> "Selesai"
        OrderStatus.DITOLAK -> "Ditolak"
    }
}

private fun formatRupiah(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))
    return "Rp. ${formatter.format(amount.toLong())}"
}