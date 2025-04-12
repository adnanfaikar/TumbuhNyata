package com.example.tumbuhnyata.data.model

import com.example.tumbuhnyata.R

data class Workshop(
    val id: String,
    val title: String,
    val speaker: String,
    val date: String,
    val imageRes: Int,
    val isOnline: Boolean,
    val tanggal: String,
    val materi: List<String>,
    val biaya: Int,
    val deskripsi: String
)

val recommendedWorkshops = listOf(
    Workshop(
        id = "1",
        title = "Sustainability Reporting 101",
        speaker = "Dr. Andi Wijaya",
        date = "10 April 2025",
        imageRes = R.drawable.work1,
        isOnline = false,
        tanggal = "10 – 11 April 2025",
        materi = listOf(
            "Prinsip dasar sustainability reporting",
            "Implementasi GRI Standards",
            "Teknik pengukuran dampak sosial dan lingkungan",
            "Studi kasus perusahaan terkemuka"
        ),
        biaya = 1500000,
        deskripsi = "Pelajari cara menyusun laporan keberlanjutan sesuai standar GRI, ISO 26000, dan SDG Reporting..."
    ),
    Workshop(
        id = "2",
        title = "Green Supply Chain Management",
        speaker = "Ir. Rina Putri, M.Sc",
        date = "15 Mei 2025",
        imageRes = R.drawable.work2,
        isOnline = true,
        tanggal = "15 – 16 Mei 2025",
        materi = listOf(
            "Konsep dasar dan evolusi CSR",
            "Peran CSR dalam pembangunan berkelanjutan",
            "Strategi pelibatan pemangku kepentingan",
            "Evaluasi dan pelaporan dampak CSR"
        ),
        biaya = 1200000,
        deskripsi = "Pelatihan mendalam untuk menyusun strategi Corporate Social Responsibility (CSR) yang berdampak jangka panjang."
    ),
    Workshop(
        id = "3",
        title = "Implementasi ISO 26000 dalam CSR",
        speaker = "Dwi Kurniawati, S.E., M.BA",
        date = "5 Juni 2025",
        imageRes = R.drawable.work3,
        isOnline = false,
        tanggal = "5 – 6 Juni 2025",
        materi = listOf(
            "Fundamental digital marketing",
            "Social media campaign & analytics",
            "Content creation dengan tujuan sosial",
            "Studi kasus brand berdampak"
        ),
        biaya = 1000000,
        deskripsi = "Belajar strategi pemasaran digital untuk memperluas jangkauan dampak sosial organisasi dan startup."
    ),
    Workshop(
        id = "4",
        title = "Strategi CSR untuk Industri Manufaktur",
        speaker = "Budi Setiawan, Ph.D",
        date = "25 Mei 2025",
        imageRes = R.drawable.work4,
        isOnline = false,
        tanggal = "25 – 26 Mei 2025",
        materi = listOf(
            "Tahapan Design Thinking",
            "Empati terhadap pengguna",
            "Ideation dan prototyping",
            "Menguji dan memvalidasi solusi"
        ),
        biaya = 1300000,
        deskripsi = "Pelajari proses Design Thinking untuk menciptakan solusi inovatif terhadap masalah sosial."
    ),
    Workshop(
        id = "5",
        title = "Mengukur Dampak CSR dengan SROI",
        speaker = "Ratna Dewi, S.Stat",
        date = "18 Juni 2025",
        imageRes = R.drawable.work5,
        isOnline = true,
        tanggal = "18 – 19 Juni 2025",
        materi = listOf(
            "Siklus proyek sosial",
            "Perencanaan dan anggaran",
            "Manajemen risiko",
            "Monitoring dan evaluasi"
        ),
        biaya = 1400000,
        deskripsi = "Workshop ini membantu peserta dalam merancang, mengelola, dan mengevaluasi proyek sosial yang berkelanjutan."
    ),
    Workshop(
        id = "6",
        title = "Penyusunan Program CSR Berkelanjutan",
        speaker = "Hendra Wijaya, M.Soc.Sc",
        date = "12 Sep 2025",
        imageRes = R.drawable.work6,
        isOnline = false,
        tanggal = "12 – 13 September 2025",
        materi = listOf(
            "Teori Perubahan (Theory of Change)",
            "KPI dan indikator dampak",
            "SROI dan metode pengukuran lainnya",
            "Pelaporan berbasis hasil"
        ),
        biaya = 1600000,
        deskripsi = "Kuasai alat dan pendekatan untuk mengukur hasil dan dampak program sosial dan keberlanjutan."
    )
)

val recentWorkshops = listOf(
    recommendedWorkshops[4],
    recommendedWorkshops[5],
    Workshop(
        id = "7",
        title = "Net Zero & Carbon Offset dalam CSR",
        speaker = "Rizky Alamsyah, M.Env",
        date = "2 Agu 2025",
        imageRes = R.drawable.work7,
        isOnline = true,
        tanggal = "2 – 4 Agustus 2025",
        materi = listOf(
            "Dasar-dasar public speaking",
            "Storytelling untuk advokasi",
            "Menghadapi audiens beragam",
            "Praktik presentasi berdampak"
        ),
        biaya = 900000,
        deskripsi = "Kembangkan kemampuan komunikasi publik untuk menyampaikan pesan perubahan sosial secara persuasif."
    ),
    Workshop(
        id = "8",
        title = "Komunikasi Publik untuk CSR Branding",
        speaker = "Siti Amalia, S.I.Kom",
        date = "12 Sep 2025",
        imageRes = R.drawable.work8,
        isOnline = false,
        tanggal = "12 – 13 September 2025",
        materi = listOf(
            "Jenis-jenis pendanaan sosial",
            "Cara membuat proposal proyek",
            "Teknik pitching yang meyakinkan",
            "Simulasi presentasi ke investor"
        ),
        biaya = 1100000,
        deskripsi = "Pelajari strategi mendapatkan pendanaan untuk inisiatif sosial, termasuk pembuatan proposal dan pitching."
    ),
    recommendedWorkshops[0],
    recommendedWorkshops[1]
)
