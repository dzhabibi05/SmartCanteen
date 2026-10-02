package com.pemmob.smartcanteen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.pemmob.smartcanteen.ui.menu.MenuCatalogScreen
import com.pemmob.smartcanteen.ui.theme.SmartCanteenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCanteenTheme {
                MenuCatalogScreen(
                    modifier = Modifier.fillMaxSize(),
                    onCheckoutClick = { cartState ->
                        Toast.makeText(
                            this,
                            "Melanjutkan ${cartState.totalCount} item ke ringkasan pesanan...",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onBottomNavTabSelected = { tab ->
                        Toast.makeText(this, "Tab: ${tab.label}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}