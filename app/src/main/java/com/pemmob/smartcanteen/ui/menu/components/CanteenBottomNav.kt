package com.pemmob.smartcanteen.ui.menu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pemmob.smartcanteen.ui.theme.CanteenNavBarBg
import com.pemmob.smartcanteen.ui.theme.CanteenPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenTextPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenTextSecondary

enum class CanteenNavTab(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    MY_ORDERS("My Orders", Icons.AutoMirrored.Filled.ReceiptLong),
    HISTORY("History", Icons.Default.BookmarkBorder),
    PROFILE("Profile", Icons.Default.PersonOutline)
}

enum class MerchantNavTab(val label: String, val icon: ImageVector) {
    ORDERS("Pesanan", Icons.AutoMirrored.Filled.ReceiptLong),
    MENU("Menu & Stok", Icons.Default.Fastfood),
    PROFILE("Profil", Icons.Default.PersonOutline)
}

@Composable
fun CanteenBottomNav(
    selectedTab: CanteenNavTab = CanteenNavTab.HOME,
    onTabSelected: (CanteenNavTab) -> Unit = {},
    modifier: Modifier = Modifier
) {
    GenericCanteenBottomNav(
        items = CanteenNavTab.entries,
        selectedItem = selectedTab,
        getItemLabel = { it.label },
        getItemIcon = { it.icon },
        onItemSelected = onTabSelected,
        modifier = modifier
    )
}

@Composable
fun MerchantBottomNav(
    selectedTab: MerchantNavTab = MerchantNavTab.ORDERS,
    onTabSelected: (MerchantNavTab) -> Unit = {},
    modifier: Modifier = Modifier
) {
    GenericCanteenBottomNav(
        items = MerchantNavTab.entries,
        selectedItem = selectedTab,
        getItemLabel = { it.label },
        getItemIcon = { it.icon },
        onItemSelected = onTabSelected,
        modifier = modifier
    )
}

@Composable
fun <T> GenericCanteenBottomNav(
    items: List<T>,
    selectedItem: T,
    getItemLabel: (T) -> String,
    getItemIcon: (T) -> ImageVector,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(32.dp),
        color = CanteenNavBarBg,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { tab ->
                val isSelected = tab == selectedItem

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onItemSelected(tab) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) CanteenPrimary.copy(alpha = 0.35f) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getItemIcon(tab),
                            contentDescription = getItemLabel(tab),
                            tint = if (isSelected) CanteenPrimary else CanteenTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Text(
                        text = getItemLabel(tab),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = if (isSelected) CanteenTextPrimary else CanteenTextSecondary
                    )
                }
            }
        }
    }
}
