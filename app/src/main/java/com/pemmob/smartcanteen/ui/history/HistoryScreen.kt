package com.pemmob.smartcanteen.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.pemmob.smartcanteen.ui.theme.CanteenSecondary
import com.pemmob.smartcanteen.ui.theme.CanteenTextPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenTextSecondary
import com.pemmob.smartcanteen.ui.theme.CanteenWarmBg
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    onBottomNavTabSelected: (CanteenNavTab) -> Unit,
    onReorderClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = viewModel()
) {
    val historyOrders by viewModel.historyOrders.collectAsState()
    val cartState by viewModel.cartState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(cartState.userNotice) {
        cartState.userNotice?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissCartNotice()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CanteenWarmBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Riwayat Pesanan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
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
            Box(modifier = Modifier.navigationBarsPadding()) {
                CanteenBottomNav(
                    selectedTab = CanteenNavTab.HISTORY,
                    onTabSelected = onBottomNavTabSelected
                )
            }
        }
    ) { innerPadding ->
        if (historyOrders.isEmpty()) {
            EmptyHistoryView(modifier = Modifier.padding(innerPadding))
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

                items(historyOrders, key = { it.id }) { order ->
                    HistoryOrderCard(
                        order = order,
                        onReorder = {
                            viewModel.reorder(order)
                            onReorderClick()
                        }
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
private fun HistoryOrderCard(
    order: Order,
    onReorder: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #${order.queueNumber}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CanteenTextPrimary
                    )
                    Text(
                        text = "Metode: ${order.paymentMethod.name}",
                        color = CanteenTextSecondary,
                        fontSize = 12.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = order.status.containerColor
                ) {
                    Text(
                        text = if (order.status == OrderStatus.SELESAI) "Selesai" else "Ditolak",
                        color = order.status.statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            // Items List Summary
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                order.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.qty}x ${item.menuName}",
                            fontSize = 13.sp,
                            color = CanteenTextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = formatRupiah(item.priceAtPurchase * item.qty),
                            fontSize = 13.sp,
                            color = CanteenTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            // Total & Reorder
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Bayar",
                        fontSize = 11.sp,
                        color = CanteenTextSecondary
                    )
                    Text(
                        text = formatRupiah(order.totalPrice),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CanteenSecondary
                    )
                }

                if (order.status == OrderStatus.SELESAI) {
                    OutlinedButton(
                        onClick = onReorder,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CanteenPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pesan Lagi", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyHistoryView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFEFEBE9), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = CanteenTextSecondary,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Belum Ada Riwayat Pesanan",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CanteenTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Pesanan yang telah selesai akan tercatat di sini",
                color = CanteenTextSecondary,
                fontSize = 13.sp
            )
        }
    }
}

private fun formatRupiah(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))
    return "Rp. ${formatter.format(amount.toLong())}"
}
