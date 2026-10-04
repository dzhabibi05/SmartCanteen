package com.pemmob.smartcanteen.data.model

import androidx.compose.ui.graphics.Color

enum class OrderStatus {
    MENUNGGU_KONFIRMASI,
    DIPROSES,
    SIAP_DIAMBIL,
    SELESAI,
    DITOLAK;

    // Menentukan apakah status sudah dalam tahap Siap Diambil / Selesai
    val isReadyOrDone: Boolean
        get() = this == SIAP_DIAMBIL || this == SELESAI

    // Warna Teks/Ikon: Orange (#F57C00) jika masih proses, Hijau (#2E7D32) jika Siap/Selesai, Merah jika Ditolak
    val statusColor: Color
        get() = when (this) {
            SIAP_DIAMBIL, SELESAI -> Color(0xFF2E7D32)
            MENUNGGU_KONFIRMASI, DIPROSES -> Color(0xFFF57C00)
            DITOLAK -> Color(0xFFC0392B)
        }

    // Warna Background Kartu Status
    val containerColor: Color
        get() = when (this) {
            SIAP_DIAMBIL, SELESAI -> Color(0xFFE8F5E9)
            MENUNGGU_KONFIRMASI, DIPROSES -> Color(0xFFFFF3E0)
            DITOLAK -> Color(0xFFFDEDEC)
        }
}

enum class PaymentMethod {
    QRIS,
    TUNAI
}