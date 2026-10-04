package com.pemmob.smartcanteen.ui.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pemmob.smartcanteen.ui.menu.components.CanteenBottomNav
import com.pemmob.smartcanteen.ui.menu.components.CanteenNavTab
import com.pemmob.smartcanteen.ui.theme.CanteenPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenSecondary
import com.pemmob.smartcanteen.ui.theme.CanteenTextPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenTextSecondary
import com.pemmob.smartcanteen.ui.theme.CanteenWarmBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBottomNavTabSelected: (CanteenNavTab) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val user by viewModel.currentUser.collectAsState()
    var showFaqDialog by remember { mutableStateOf(false) }

    if (showFaqDialog) {
        FaqHelpDialog(
            onDismiss = { showFaqDialog = false }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CanteenWarmBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profil Saya",
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
                    selectedTab = CanteenNavTab.PROFILE,
                    onTabSelected = onBottomNavTabSelected
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // User Header Card
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
                            .background(CanteenPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = CanteenPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = CanteenTextPrimary
                        )
                        Text(
                            text = user.email,
                            fontSize = 13.sp,
                            color = CanteenTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CanteenSecondary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Mahasiswa / Pembeli",
                                color = CanteenSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Info Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ProfileItemRow(
                        icon = Icons.Default.Badge,
                        label = "NIM / ID Mahasiswa",
                        value = "220101001"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))
                    ProfileItemRow(
                        icon = Icons.Default.Email,
                        label = "Email Akun",
                        value = user.email
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))
                    ProfileItemRow(
                        icon = Icons.Default.Storefront,
                        label = "Kantin Langganan",
                        value = "Kantin Utama - Stand 01"
                    )
                }
            }

            // Options & Logout
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    ProfileOptionRow(
                        icon = Icons.AutoMirrored.Filled.HelpOutline,
                        title = "Bantuan & Pertanyaan FAQ",
                        onClick = { showFaqDialog = true }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFF0F0F0))
                    ProfileOptionRow(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        title = "Ganti Peran / Keluar Akun",
                        titleColor = MaterialTheme.colorScheme.error,
                        onClick = onLogoutClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileItemRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CanteenTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = CanteenTextSecondary
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = CanteenTextPrimary
            )
        }
    }
}

@Composable
private fun ProfileOptionRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    titleColor: Color = CanteenTextPrimary
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = titleColor,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = titleColor
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

@Composable
private fun FaqHelpDialog(
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
                            imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                            contentDescription = null,
                            tint = CanteenSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Bantuan & FAQ",
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

                val faqs = listOf(
                    "📌 Bagaimanakah cara mengambil pesanan?" to "Tunjukkan nomor antrean Anda di layar Order Tracking ke kasir kantin saat status berubah menjadi 'Siap Diambil'.",
                    "💳 Metode pembayaran apa saja yang bisa digunakan?" to "Dapat memilih pembayaran QRIS (non-tunai) atau Tunai langsung di kasir.",
                    "❌ Bagaimana jika pesanan saya ditolak?" to "Jika pesanan ditolak penjual, stok porsi akan dipulihkan secara otomatis dan Anda tidak dikenakan biaya."
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    faqs.forEach { (q, a) ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CanteenWarmBg),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = q,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = CanteenTextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = a,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    color = CanteenTextSecondary
                                )
                            }
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
