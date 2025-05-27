package com.example.tumbuhnyata.data.model

import androidx.compose.ui.graphics.Color

enum class Status {
    DITERIMA,
    DITOLAK,
    PENDING
}

enum class SubStatus {
    MENUNGGU_PEMBAYARAN,
    MEMERLUKAN_REVISI,
    PROSES_REVIEW,
    MENDATANG,
    PROGRESS,
    SELESAI
}

data class CsrItem(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val status: Status,
    val subStatus: SubStatus,
    val tagColor: Color,
    val imageRes: Int? = null
)

// Data dummy untuk testing
val dummyCsrList = listOf(
    CsrItem(
        id = "CSR-001",
        title = "Program Penanaman Pohon",
        date = "12 Feb 2024",
        location = "Kota Bogor",
        status = Status.DITERIMA,
        subStatus = SubStatus.PROGRESS,
        tagColor = Color(0xFF4CAF50)
    ),
    CsrItem(
        id = "CSR-002",
        title = "Workshop Literasi Digital",
        date = "20 Feb 2024",
        location = "Jakarta Selatan",
        status = Status.PENDING,
        subStatus = SubStatus.PROSES_REVIEW,
        tagColor = Color(0xFFFF9800)
    ),
    CsrItem(
        id = "CSR-003",
        title = "Bantuan Pendidikan",
        date = "28 Feb 2024",
        location = "Surabaya",
        status = Status.PENDING,
        subStatus = SubStatus.MENUNGGU_PEMBAYARAN,
        tagColor = Color(0xFFFF5722)
    ),
    CsrItem(
        id = "CSR-004",
        title = "Pengelolaan Sampah",
        date = "5 Mar 2024",
        location = "Bandung",
        status = Status.DITERIMA,
        subStatus = SubStatus.MENDATANG,
        tagColor = Color(0xFF2196F3)
    ),
    CsrItem(
        id = "CSR-005",
        title = "Pelatihan UMKM",
        date = "15 Mar 2024",
        location = "Yogyakarta",
        status = Status.DITERIMA,
        subStatus = SubStatus.SELESAI,
        tagColor = Color(0xFF9C27B0)
    ),
    CsrItem(
        id = "CSR-006",
        title = "Donasi Kesehatan",
        date = "22 Mar 2024",
        location = "Semarang",
        status = Status.PENDING,
        subStatus = SubStatus.MEMERLUKAN_REVISI,
        tagColor = Color(0xFFE91E63)
    ),
) 