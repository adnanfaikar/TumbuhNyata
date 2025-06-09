package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.api.CertificationApi
import com.example.tumbuhnyata.data.model.iso26000
import com.example.tumbuhnyata.ui.components.TopBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DokumenOne(navController: NavController) {
    var fileCSR by remember { mutableStateOf<String?>(null) }
    var fileLingkungan by remember { mutableStateOf<String?>(null) }
    var fileImplementasi by remember { mutableStateOf<String?>(null) }
    var filePernyataan by remember { mutableStateOf<String?>(null) }

    val isBothFilesUploaded = fileCSR != null && fileLingkungan != null && fileImplementasi != null && filePernyataan != null

    var userId by remember { mutableStateOf("1") }
    var name by remember { mutableStateOf(iso26000.nama) }
    var description by remember { mutableStateOf(iso26000.deskripsi) }
    var credentialBody by remember { mutableStateOf(iso26000.lembaga) }
    var benefits by remember { mutableStateOf(iso26000.manfaat.joinToString(", ")) }
    var cost by remember { mutableStateOf(iso26000.biaya.toString()) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    suspend fun registerSertifikasiToBackend() : Boolean {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(CertificationApi::class.java)
        val body = mapOf(
            "user_id" to userId,
            "name" to name,
            "description" to description,
            "credential_body" to credentialBody,
            "benefits" to benefits,
            "cost" to cost,
            "supporting_documents" to listOf<String>()

        )
        val response = service.submitCertification(body)
        return response.isSuccessful
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, top = 40.dp, end = 20.dp)
        ) {
            TopBarProfile(
                title = "Verifikasi",
                step = "",
                iconResId = R.drawable.btn_back,
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Unggah Dokumen Pendukung",
                fontSize = 22.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 2.dp)
            )
            Text(
                text = "Unggah dokumen pendukung untuk melanjutkan pengajuan sertifikasi CSR",
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4B4B4B),
                modifier = Modifier.padding(start = 2.dp, top = 10.dp, end = 5.dp)
            )

            UploadField(
                fileName = fileCSR,
                placeholder = "Akta Pendirian",
                onUploadClick = { fileCSR = "CSR_Report_2023.pdf" }, // Simulasi upload
                onDelete = { fileCSR = null },
                modifier = Modifier.padding(top=40.dp)
            )

            UploadField(
                fileName = fileLingkungan,
                placeholder = "SKDP",
                onUploadClick = { fileLingkungan = "Green_Policy_Paragon.pdf" }, // Simulasi upload
                onDelete = { fileLingkungan = null },
                modifier = Modifier.padding(top=20.dp)
            )
            UploadField(
                fileName = fileImplementasi,
                placeholder = "Akta Pendirian",
                onUploadClick = { fileImplementasi = "Project_TreePlanting.pdf" }, // Simulasi upload
                onDelete = { fileImplementasi = null },
                modifier = Modifier.padding(top=40.dp)
            )

            UploadField(
                fileName = filePernyataan,
                placeholder = "SKDP",
                onUploadClick = { filePernyataan = "Compliance_Declaration.pdf" }, // Simulasi upload
                onDelete = { filePernyataan = null },
                modifier = Modifier.padding(top=20.dp)
            )

            Text(
                text = "*Jika perusahaan anda tidak mempunyai beberapa laporan di atas anda bisa mendownload template di sini",
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4B4B4B),
                modifier = Modifier.padding(start = 2.dp, top = 10.dp, end = 5.dp)
            )

            Spacer(modifier = Modifier.height(150.dp))

            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = registerSertifikasiToBackend()
                        withContext(Dispatchers.Main) {
                            isLoading = false
                            if (result) {
                                navController.navigate("berhasil")
                            } else {
                                errorMessage = "Gagal Mendaftarkan Sertifikasi"
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(if (isBothFilesUploaded) Color(0xFF27361F) else Color(0xFF989898)
                ),
                enabled = isBothFilesUploaded,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Selanjutnya",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun UploadField(
    fileName: String?,
    placeholder: String,
    onUploadClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .border(2.dp, Color(0xFFB9B9B9), shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp)
                .clickable { onUploadClick() },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = if (fileName != null) R.drawable.ic_doc else R.drawable.ic_upload),
                    contentDescription = "Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = fileName ?: placeholder,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = if (fileName != null) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (fileName != null) Color(0xFF686868) else Color(0xFFB8B8B8),
                    modifier = Modifier.weight(1f)
                )

                if (fileName != null) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription = "Delete",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDelete() }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDokumenOne() {
    DokumenOne(navController = rememberNavController())
}