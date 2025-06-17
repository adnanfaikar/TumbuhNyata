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
    SELESAI("Program Selesai", "#989898")
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
        status = "Akan Datang",
        subStatus = SubStatus.MENDATANG,
        category = "Lingkungan",
        location = "Kalimantan",
        period = "12 Mei - 20 Mei 25"
    ),
    CsrItem(
        title = "Beasiswa Yatim Jabar",
        organization = "Pemerintah Prov. Jabar",
        status = "Sedang Berlangsung",
        subStatus = SubStatus.PROGRESS,
        category = "Sosial",
        location = "Jawa Barat",
        period = "8 Mar - 10 Jun 25"
    ),
    CsrItem(
        title = "Donor Darah Paragon 2025",
        organization = "RS Bunda Mulia",
        status = "Sedang Berlangsung",
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

/**
 * Helper functions untuk kategori status CSR
 */
fun isPerluTindakanStatus(status: String): Boolean {
    return status in listOf("Proses Review", "Memerlukan Revisi", "Menunggu Pembayaran")
}

fun isDiterimaStatus(status: String): Boolean {
    return status in listOf("Akan Datang", "Sedang Berlangsung", "Program Selesai")
}

/**
 * Status constants untuk konsistensi
 */
object CsrStatusConstants {
    // Perlu Tindakan statuses
    const val PROSES_REVIEW = "Proses Review"
    const val MEMERLUKAN_REVISI = "Memerlukan Revisi" 
    const val MENUNGGU_PEMBAYARAN = "Menunggu Pembayaran"
    
    // Diterima statuses
    const val AKAN_DATANG = "Akan Datang"
    const val SEDANG_BERLANGSUNG = "Sedang Berlangsung"
    const val PROGRAM_SELESAI = "Program Selesai"
    
    val PERLU_TINDAKAN_STATUSES = listOf(PROSES_REVIEW, MEMERLUKAN_REVISI, MENUNGGU_PEMBAYARAN)
    val DITERIMA_STATUSES = listOf(AKAN_DATANG, SEDANG_BERLANGSUNG, PROGRAM_SELESAI)
    val ALL_STATUSES = PERLU_TINDAKAN_STATUSES + DITERIMA_STATUSES
}

/**
 * Normalisasi status input - jika tidak sesuai dengan 6 status valid, default ke "Proses Review"
 */
fun normalizeStatus(inputStatus: String): String {
    val normalizedInput = inputStatus.trim()
    
    // Cek apakah status sudah valid (case-insensitive)
    val validStatus = CsrStatusConstants.ALL_STATUSES.find { 
        it.equals(normalizedInput, ignoreCase = true) 
    }
    
    return if (validStatus != null) {
        validStatus // Return proper case version
    } else {
        // Log status yang tidak dikenal untuk debugging
        android.util.Log.w("CsrStatus", "Unknown status '$inputStatus' normalized to '${CsrStatusConstants.PROSES_REVIEW}'")
        CsrStatusConstants.PROSES_REVIEW
    }
}

/**
 * Convert status ke SubStatus enum dengan fallback
 */
fun statusToSubStatus(status: String): SubStatus {
    val normalizedStatus = normalizeStatus(status)
    
    return when (normalizedStatus.lowercase().trim()) {
        "proses review" -> SubStatus.PROSES_REVIEW
        "memerlukan revisi" -> SubStatus.MEMERLUKAN_REVISI
        "menunggu pembayaran" -> SubStatus.MENUNGGU_PEMBAYARAN
        "akan datang" -> SubStatus.MENDATANG
        "sedang berlangsung" -> SubStatus.PROGRESS
        "program selesai" -> SubStatus.SELESAI
        else -> SubStatus.PROSES_REVIEW
    }
}

/**
 * Test examples untuk status normalization:
 * 
 * Input: "proses review" → Output: "Proses Review"
 * Input: "AKAN DATANG" → Output: "Akan Datang"
 * Input: "invalid status" → Output: "Proses Review"
 * Input: "  sedang berlangsung  " → Output: "Sedang Berlangsung"
 * Input: "review" → Output: "Proses Review"
 * Input: "" → Output: "Proses Review"
 */

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
            isCompleted = isDiterimaStatus(status),
            isInProgress = isPerluTindakanStatus(status)
        ),
        TimelineItem(
            title = "Pembayaran",
            timestamp = "10/05/2024 - 10:00 WIB",
            isCompleted = status in listOf("Sedang Berlangsung", "Program Selesai")
        ),
        TimelineItem(
            title = "Implementasi Program",
            isCompleted = status == "Program Selesai"
        )
    )
}