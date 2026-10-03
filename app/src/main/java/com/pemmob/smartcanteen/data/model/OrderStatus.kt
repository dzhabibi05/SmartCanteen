package com.pemmob.smartcanteen.data.model

enum class OrderStatus {
    MENUNGGU_KONFIRMASI,
    DIPROSES,
    SIAP_DIAMBIL,
    SELESAI,
    DITOLAK
}

enum class PaymentMethod {
    QRIS,
    TUNAI
}