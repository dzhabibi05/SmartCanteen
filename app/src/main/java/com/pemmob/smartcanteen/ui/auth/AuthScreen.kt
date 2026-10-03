package com.pemmob.smartcanteen.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pemmob.smartcanteen.data.model.User
import com.pemmob.smartcanteen.data.model.UserRole
import com.pemmob.smartcanteen.data.repository.FakeCanteenRepository
import com.pemmob.smartcanteen.ui.theme.CanteenPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenSecondary
import com.pemmob.smartcanteen.ui.theme.CanteenTextPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenTextSecondary
import com.pemmob.smartcanteen.ui.theme.CanteenWarmBg

@Composable
fun AuthScreen(
    onNavigateToBuyer: () -> Unit,
    onNavigateToMerchant: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = CanteenWarmBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SmartCanteen",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                ),
                color = CanteenPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pilih peran Anda untuk masuk",
                style = MaterialTheme.typography.bodyMedium,
                color = CanteenTextSecondary
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    FakeCanteenRepository.switchUserRole(
                        User("user_mhs_1", "Yoga", "yoga@canteen.ac.id", UserRole.MAHASISWA)
                    )
                    onNavigateToBuyer()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CanteenPrimary)
            ) {
                Text(
                    text = "Masuk sebagai Mahasiswa (Pembeli)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    FakeCanteenRepository.switchUserRole(
                        User("seller_1", "Kantin Barokah", "kantin@canteen.ac.id", UserRole.PENJUAL)
                    )
                    onNavigateToMerchant()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CanteenSecondary)
            ) {
                Text(
                    text = "Masuk sebagai Penjual (Merchant)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}