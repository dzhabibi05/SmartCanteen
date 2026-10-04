package com.pemmob.smartcanteen.ui.merchant.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pemmob.smartcanteen.data.model.MenuItem
import com.pemmob.smartcanteen.ui.theme.CanteenBorder
import com.pemmob.smartcanteen.ui.theme.CanteenSecondary
import com.pemmob.smartcanteen.ui.theme.CanteenTextPrimary
import com.pemmob.smartcanteen.ui.theme.CanteenTextSecondary

@Composable
fun AddEditMenuDialog(
    menuItemToEdit: MenuItem? = null,
    onDismiss: () -> Unit,
    onSaveNew: (name: String, price: Double, stock: Int, category: String, description: String, photoUrl: String) -> Unit,
    onSaveUpdated: (MenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val isEditMode = menuItemToEdit != null

    var name by remember { mutableStateOf(menuItemToEdit?.name ?: "") }
    var priceText by remember { mutableStateOf(menuItemToEdit?.price?.toInt()?.toString() ?: "") }
    var stockText by remember { mutableStateOf(menuItemToEdit?.stock?.toString() ?: "10") }
    var category by remember { mutableStateOf(menuItemToEdit?.category ?: "Lunch") }
    var description by remember { mutableStateOf(menuItemToEdit?.description ?: "") }
    var photoUrl by remember { mutableStateOf(menuItemToEdit?.photoUrl ?: "") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = if (isEditMode) "Edit Menu" else "Tambah Menu Baru",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = CanteenTextPrimary
                )

                errorMessage?.let { error ->
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                // Field Nama
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Menu") },
                    placeholder = { Text("Contoh: Nasi Goreng Spesial") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CanteenSecondary,
                        unfocusedBorderColor = CanteenBorder
                    )
                )

                // Field Harga & Stok
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it.filter { char -> char.isDigit() } },
                        label = { Text("Harga (Rp)") },
                        placeholder = { Text("15000") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CanteenSecondary,
                            unfocusedBorderColor = CanteenBorder
                        )
                    )

                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { stockText = it.filter { char -> char.isDigit() } },
                        label = { Text("Stok Porsi") },
                        placeholder = { Text("10") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CanteenSecondary,
                            unfocusedBorderColor = CanteenBorder
                        )
                    )
                }

                // Field Kategori
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Kategori (Lunch/Breakfast/Drinks/Snacks)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CanteenSecondary,
                        unfocusedBorderColor = CanteenBorder
                    )
                )

                // Field Deskripsi
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi Menu") },
                    placeholder = { Text("Komposisi & rasa menu...") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CanteenSecondary,
                        unfocusedBorderColor = CanteenBorder
                    )
                )

                // Field Photo URL
                OutlinedTextField(
                    value = photoUrl,
                    onValueChange = { photoUrl = it },
                    label = { Text("URL Foto (Opsional)") },
                    placeholder = { Text("https://...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CanteenSecondary,
                        unfocusedBorderColor = CanteenBorder
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Batal", color = CanteenTextSecondary)
                    }

                    Spacer(modifier = Modifier.height(0.dp))

                    Button(
                        onClick = {
                            val parsedPrice = priceText.toDoubleOrNull()
                            val parsedStock = stockText.toIntOrNull()

                            if (name.isBlank()) {
                                errorMessage = "Nama menu tidak boleh kosong."
                                return@Button
                            }
                            if (parsedPrice == null || parsedPrice <= 0) {
                                errorMessage = "Harga harus berupa angka lebih dari 0."
                                return@Button
                            }
                            if (parsedStock == null || parsedStock < 0) {
                                errorMessage = "Stok harus berupa angka 0 atau lebih."
                                return@Button
                            }

                            if (isEditMode && menuItemToEdit != null) {
                                val updated = menuItemToEdit.copy(
                                    name = name.trim(),
                                    price = parsedPrice,
                                    stock = parsedStock,
                                    category = category.ifBlank { "Lunch" }.trim(),
                                    description = description.trim(),
                                    photoUrl = photoUrl.ifBlank { menuItemToEdit.photoUrl }.trim(),
                                    isActive = parsedStock > 0
                                )
                                onSaveUpdated(updated)
                            } else {
                                onSaveNew(
                                    name.trim(),
                                    parsedPrice,
                                    parsedStock,
                                    category.ifBlank { "Lunch" }.trim(),
                                    description.trim(),
                                    photoUrl.trim()
                                )
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CanteenSecondary),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = if (isEditMode) "Simpan Perubahan" else "Tambah Menu",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
