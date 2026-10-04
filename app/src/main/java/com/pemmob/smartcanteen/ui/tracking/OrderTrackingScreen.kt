package com.pemmob.smartcanteen.ui.tracking

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pemmob.smartcanteen.data.model.OrderStatus
import com.pemmob.smartcanteen.ui.theme.CanteenPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenWarmBg
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    orderId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TrackingViewModel = viewModel()
) {
    val order by viewModel.observeOrder(orderId).collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CanteenWarmBg,
        topBar = {
            TopAppBar(
                title = { Text("Your Order", fontWeight = FontWeight.Bold, fontSize = 28.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CanteenWarmBg)
            )
        }
    ) { innerPadding ->
        if (order == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CanteenPrimary)
            }
        } else {
            val currentOrder = order!!
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Top Banner Status Update (Orange Header)
                item {
                    BannerStatusHeader(status = currentOrder.status)
                }

                // 2. Order Detail Card (Menampilkan data asli dari order)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            // Header Nomor Antrean (Dine-in badge telah dihapus)
                            Column {
                                Text(
                                    text = "Order #${currentOrder.queueNumber}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Estimasi: ± ${currentOrder.estimatedMinutes} menit",
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color(0xFFF0F0F0))
                            Spacer(modifier = Modifier.height(16.dp))

                            // List Item Makanan Dinamis dari currentOrder.items
                            currentOrder.items.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
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
                                            text = item.menuName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = "Qty: ${item.qty}",
                                            fontSize = 13.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Text(
                                        text = formatRupiah(item.subtotal),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Status Section Header
                item {
                    Text(
                        text = "Status",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // 4. Horizontal Timeline Status Card
                item {
                    HorizontalStatusTimelineCard(currentStatus = currentOrder.status)
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun BannerStatusHeader(status: OrderStatus) {
    val titleText = when (status) {
        OrderStatus.MENUNGGU_KONFIRMASI -> "Menunggu konfirmasi pembayaran!"
        OrderStatus.DIPROSES -> "Pesanan sedang disiapkan!"
        OrderStatus.SIAP_DIAMBIL -> "Pesanan siap diambil di kantin!"
        OrderStatus.SELESAI -> "Pesanan selesai!"
        OrderStatus.DITOLAK -> "Pesanan ditolak penjual"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = status.containerColor)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(status.statusColor.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Fastfood,
                    contentDescription = null,
                    tint = status.statusColor
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Update Pesanan",
                    color = status.statusColor.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = titleText,
                    color = status.statusColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun HorizontalStatusTimelineCard(currentStatus: OrderStatus) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            val steps = listOf(
                OrderStatus.MENUNGGU_KONFIRMASI to "Menunggu\nKonfirmasi",
                OrderStatus.DIPROSES to "Pesanan\nDiproses",
                OrderStatus.SIAP_DIAMBIL to "Siap\nDiambil",
                OrderStatus.SELESAI to "Pesanan\nSelesai"
            )

            val currentIndex = steps.indexOfFirst { it.first == currentStatus }

            // Garis Penghubung Belakang
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.Center),
                color = Color(0xFFECECEE),
                thickness = 2.dp
            )

            // Step Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, pair ->
                    val stepStatus = pair.first
                    val isCurrent = index == currentIndex
                    val isPassed = index < currentIndex

                    val activeBg = stepStatus.statusColor
                    val bg = when {
                        isCurrent -> activeBg
                        isPassed -> activeBg.copy(alpha = 0.3f)
                        else -> Color(0xFFF3ECE5)
                    }

                    val textColor = if (isCurrent) Color.White else Color(0xFF6E6E6E)

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = bg,
                        modifier = Modifier.width(80.dp)
                    ) {
                        Text(
                            text = pair.second,
                            color = textColor,
                            fontSize = 9.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            lineHeight = 11.sp,
                            modifier = Modifier.padding(horizontal = 2.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun formatRupiah(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))
    return "Rp. ${formatter.format(amount.toLong())}"
}