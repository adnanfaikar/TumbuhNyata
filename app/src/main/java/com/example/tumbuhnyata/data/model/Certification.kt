package com.example.tumbuhnyata.data.model

import com.example.tumbuhnyata.R
import com.google.gson.annotations.SerializedName

data class Sertifikasi(
    val nama: String,
    val lembaga: String,
    val deskripsi: String,
    val manfaat: List<String>,
    val biaya: Int,
    val logoResId: Int
)

data class Certification(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("credential_body")
    val credentialBody: String?,

    @SerializedName("benefits")
    val benefits: String?,

    @SerializedName("cost")
    val cost: Double?,

    @SerializedName("status")
    val status: String, // Values: "submitted", "approved", "rejected", "in_review"

    @SerializedName("submission_date")
    val submissionDate: String, // Gunakan format ISO 8601 (contoh: "2025-06-05T14:30:00")

    @SerializedName("supporting_documents")
    val supportingDocuments: List<String>?, // Asumsinya berupa list JSON berisi URL/file path

    @SerializedName("created_at")
    val createdAt: String?
)

val iso26000 = Sertifikasi(
    nama = "ISO 26000 - Sustainability",
    lembaga = "International Organization for Standardization (ISO)",
    deskripsi = "Standar global untuk tanggung jawab sosial perusahaan, mencakup tata kelola, hak asasi manusia, lingkungan, praktik ketenagakerjaan, dan keterlibatan masyarakat.",
    manfaat = listOf(
        "Meningkatkan reputasi perusahaan",
        "Memastikan kepatuhan terhadap standar internasional",
        "Meningkatkan daya saing bisnis"
    ),
    biaya = 25000000,
    logoResId = R.drawable.iso_26000

)

data class CertificationResponse(
    @SerializedName("data")
    val data: Certification
)
