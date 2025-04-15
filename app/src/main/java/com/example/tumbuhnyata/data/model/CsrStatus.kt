package com.example.tumbuhnyata.data.model

data class CsrItem(
    val title: String,
    val organization: String,
    val status: String,
    val subStatus: SubStatus,
    val category: String,
    val location: String,
    val period: String
)

fun getSubStatusEmoji(status: SubStatus): String {
    return when (status) {
        SubStatus.PROSES_REVIEW -> "🔍"
        SubStatus.MENUNGGU_PEMBAYARAN -> "💳"
        SubStatus.MEMERLUKAN_REVISI -> "📝"
        SubStatus.MENDATANG -> "📅"
        SubStatus.PROGRESS -> "⏳"
        SubStatus.SELESAI -> "✅"
    }
}

enum class SubStatus(val displayText: String, val colorHex: String) {
    PROSES_REVIEW("Proses Review", "#1E1E1E"),
    MENUNGGU_PEMBAYARAN("Menunggu Pembayaran", "#86FF8E"),
    MEMERLUKAN_REVISI("Memerlukan Revisi", "#FF7B7B"),
    MENDATANG("Akan Datang", "#9CDEFF"),
    PROGRESS("Sedang Berlangsung", "#FFD95D"),
    SELESAI("Selesai", "#989898")
}

val dummyCsrList = listOf(
    CsrItem(
        title = "Penanaman 1000 Pohon",
        organization = "Komunitas Jaya Hijau",
        status = "Proses Review",
        subStatus = SubStatus.PROSES_REVIEW,
        category = "Lingkungan",
        location = "Jakarta Timur",
        period = "12 Apr - 20 Jun 24"
    ),

    CsrItem(
        title = "Pembangunan Panti Sukamaju",
        organization = "Yayasan Asih Jaya",
        status = "Menunggu Pembayaran",
        subStatus = SubStatus.MENUNGGU_PEMBAYARAN,
        category = "Sosial",
        location = "Jakarta Raya",
        period = "1 Apr - 20 Jun 24"
    ),

    CsrItem(
        title = "Penghijauan Suaka Jateng",
        organization = "CV. Budi Raya",
        status = "Memerlukan Revisi",
        subStatus = SubStatus.MEMERLUKAN_REVISI,
        category = "Lingkungan",
        location = "Semarang, Jawa Tengah",
        period = "12 Mar - 20 Jun 24"
    ),
    CsrItem(
        title = "Penghijauan Hutan Kaltim",
        organization = "PT Hijau Sejati",
        status = "Mendatang",
        subStatus = SubStatus.MENDATANG,
        category = "Lingkungan",
        location = "Kalimantan",
        period = "12 Mei - 20 Mei 25"
    ),
    CsrItem(
        title = "Beasiswa Yatim Jabar",
        organization = "Pemerintah Prov. Jabar",
        status = "Progress",
        subStatus = SubStatus.PROGRESS,
        category = "Sosial",
        location = "Jawa Barat",
        period = "8 Mar - 10 Jun 25"
    ),
    CsrItem(
        title = "Donor Darah Paragon 2025",
        organization = "RS Bunda Mulia",
        status = "Progress",
        subStatus = SubStatus.PROGRESS,
        category = "Sosial",
        location = "Jakarta Raya",
        period = "12 Jan - 2 Apr 25"
    ),
    CsrItem(
        title = "Penanaman Mangrove",
        organization = "Pemkot Kota Lombok",
        status = "Program Selesai",
        subStatus = SubStatus.SELESAI,
        category = "Lingkungan",
        location = "Lombok Barat, Lombok",
        period = "12 Mar - 20 Jun 24"
    ),

)

fun getDummyTimelineData(status: String): List<TimelineItem> {
    return listOf(
        TimelineItem(
            title = "Pengajuan Dikirim",
            timestamp = "10/05/2024 - 09:41 WIB",
            isCompleted = true
        ),
        TimelineItem(
            title = "Review & Evaluasi",
            timestamp = "10/05/2024 - 09:50 WIB",
            isCompleted = status.lowercase() == "progress" || status.lowercase() == "diterima",
            isInProgress = status.lowercase() == "progress"
        ),
        TimelineItem(
            title = "Pembayaran",
            timestamp = "10/05/2024 - 10:00 WIB",
            isCompleted = status.lowercase() == "diterima"
        ),
        TimelineItem(
            title = "Implementasi Program",
            isCompleted = status.lowercase() == "diterima"
        )
    )
}