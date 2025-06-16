package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.TopBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import androidx.compose.ui.draw.clip

// Data class for available certifications
data class AvailableCertification(
    val id: String,
    val name: String,
    val description: String,
    val credentialBody: String,
    val benefits: String,
    val cost: String,
    val imageRes: Int
)

@Composable
fun AjukanSertifikasiScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 30.dp, end = 20.dp)
    ) {
        TopBarProfile(
            title = "Ajukan Sertifikasi",
            step = "",
            iconResId = R.drawable.btn_back,
            onBackClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Sertifikasi Tersedia",
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily,
            fontSize = 21.sp,
            color = Color(0xFF1A4218)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CertificationListSection(navController)
    }
}

@Composable
fun CertificationListSection(navController: NavController) {
    // Hardcoded data for available certifications
    val availableCertifications = listOf(
        AvailableCertification(
            id = "CSR_ISO_26000",
            name = "Sertifikat CSR ISO 26000",
            description = "Sertifikasi untuk tanggung jawab sosial perusahaan sesuai dengan standar ISO 26000. Meningkatkan reputasi perusahaan dalam aspek keberlanjutan dan tanggung jawab sosial.",
            credentialBody = "Lembaga Sertifikasi Nasional",
            benefits = "Meningkatkan kredibilitas perusahaan dalam praktik CSR dan keberlanjutan",
            cost = "1500000.0",
            imageRes = R.drawable.iso_26000
        ),
        AvailableCertification(
            id = "ENV_ISO_14001",
            name = "Environmental Management ISO 14001",
            description = "Sertifikasi sistem manajemen lingkungan yang membantu organisasi mengelola dampak lingkungan secara efektif dan berkelanjutan.",
            credentialBody = "International Standards Organization",
            benefits = "Mengurangi dampak lingkungan dan meningkatkan efisiensi operasional",
            cost = "3000000.0",
            imageRes = R.drawable.iso_14001
        ),
        AvailableCertification(
            id = "PROPER_CERT",
            name = "PROPER Certification",
            description = "Program Penilaian Peringkat Kinerja Perusahaan dalam Pengelolaan Lingkungan untuk mengukur ketaatan perusahaan terhadap peraturan lingkungan.",
            credentialBody = "Kementerian Lingkungan Hidup",
            benefits = "Meningkatkan reputasi perusahaan dalam pengelolaan lingkungan",
            cost = "2800000.0",
            imageRes = R.drawable.proper
        ),
        AvailableCertification(
            id = "ECOLABEL_CERT",
            name = "EcoLabel Certification",
            description = "Sertifikasi label ramah lingkungan yang mengakui produk atau layanan dengan dampak lingkungan minimal.",
            credentialBody = "Green Certification Body",
            benefits = "Mengakui komitmen perusahaan terhadap produk ramah lingkungan",
            cost = "2200000.0",
            imageRes = R.drawable.ecolabel
        ),
        AvailableCertification(
            id = "ISCC_CERT",
            name = "ISCC Sustainability Certification",
            description = "International Sustainability and Carbon Certification untuk memastikan keberlanjutan dalam rantai pasokan global.",
            credentialBody = "ISCC System GmbH",
            benefits = "Memastikan keberlanjutan rantai pasokan dan mengurangi jejak karbon",
            cost = "2700000.0",
            imageRes = R.drawable.iscc
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        availableCertifications.forEach { certification ->
            AvailableCertificationCard(
                data = certification,
                onSelect = {
                    // Navigate to DokumenOne with certification data
                    navController.navigate(
                        "dokumenone/${certification.id}/${certification.name}/${certification.description}/${certification.credentialBody}/${certification.benefits}/${certification.cost}"
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AvailableCertificationCard(
    data: AvailableCertification,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Certificate image
                Image(
                    painter = painterResource(id = data.imageRes),
                    contentDescription = "Certificate",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Certificate information
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = data.name,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily,
                        fontSize = 16.sp,
                        color = Color(0xFF1A4218)
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Penerbit: ${data.credentialBody}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Biaya: Rp ${String.format("%,.0f", data.cost.toDouble())}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = Color(0xFF1A4218),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = data.description,
                fontFamily = PoppinsFontFamily,
                fontSize = 14.sp,
                color = Color(0xFF333333),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Benefits
            Text(
                text = "Manfaat: ${data.benefits}",
                fontFamily = PoppinsFontFamily,
                fontSize = 12.sp,
                color = Color(0xFF666666),
                lineHeight = 16.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Click to apply hint
            Text(
                text = "👆 Klik untuk ajukan sertifikasi ini",
                fontFamily = PoppinsFontFamily,
                fontSize = 12.sp,
                color = Color(0xFF1A4218),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAjukanSertifikasiScreen() {
    val navController = rememberNavController()
    AjukanSertifikasiScreen(navController)
}package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.TopBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun AjukanSertifikasiScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 95.dp, end = 20.dp)
    ) {
        TopBarProfile(
            title = "Ajukan Sertifikasi",
            step = "",
            iconResId = R.drawable.btn_back,
            onBackClick = { }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Pilih Sertifikasi Anda",
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily,
            fontSize = 21.sp,
            color = Color(0xFF1A4218)
        )

        AjukanSertifikasiSection()
    }
}

data class AjukanSertifikasi(
    val title: String,
    val code: String,
    val deskripsi: String,
    val imageRes: Int
)

@Composable
fun AjukanSertifikasiSection() {
    val sertifikasiList = listOf(
        AjukanSertifikasi(
            title = "Social Responsibility",
            code = "ISO 26000",
            deskripsi = "International Organization for Standardization (ISO)",

            imageRes = R.drawable.iso_26000
        ),
        AjukanSertifikasi(
            title = "PROPER",
            code = "Program Penilaian Peringkat Kinerja Perusahaan dalam Pengelolaan Lingkungan",
            deskripsi = "Kementerian Lingkungan Hidup dan Kehutanan (KLHK)",

            imageRes = R.drawable.proper
        ),
        AjukanSertifikasi(
            title = "Ecolabel Indonesia",
            code = "Ecolabel",
            deskripsi = "Kementerian Perindustrian RI",

            imageRes = R.drawable.ecolabel
        ),
        AjukanSertifikasi(
                title = "Social Accountability Certification",
        code = "SA8000",
        deskripsi = "Social Accountability International (SAI)",

        imageRes = R.drawable.sai
    )
    )

    Spacer(modifier = Modifier.height(8.dp))

    sertifikasiList.forEach {
        AjukanSertifikasiCard(it)
        Spacer(modifier = Modifier.height(12.dp))
    }
}


@Composable
fun AjukanSertifikasiCard(data: AjukanSertifikasi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = data.imageRes),
                contentDescription = "Sertifikasi Icon",
                modifier = Modifier
                    .size(78.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(data.title, fontWeight = FontWeight.Bold, fontFamily = PoppinsFontFamily, fontSize = 17.sp)
                Text(data.code, fontSize = 14.sp, fontFamily = PoppinsFontFamily, color = Color.Gray)
                Text(data.deskripsi, fontFamily = PoppinsFontFamily, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAjukanSertifikasiScreen() {
    AjukanSertifikasiScreen()
}