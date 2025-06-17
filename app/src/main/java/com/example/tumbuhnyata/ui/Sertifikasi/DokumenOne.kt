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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.TopBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.data.factory.CertificationFactory
import com.example.tumbuhnyata.data.factory.CertificationViewModelFactory
import com.example.tumbuhnyata.viewmodel.AjukanSertifikasiViewModel
import java.text.SimpleDateFormat
import java.util.*

// Data class for certification options
data class CertificationOption(
    val id: String,
    val name: String,
    val description: String,
    val credentialBody: String,
    val benefits: String,
    val cost: String,
    val imageRes: Int
)

@Composable
fun DokumenOne(
    navController: NavController,
    certificationId: String? = null,
    certificationName: String? = null,
    certificationDescription: String? = null,
    certificationCredentialBody: String? = null,
    certificationBenefits: String? = null,
    certificationCost: String? = null
) {
    val context = LocalContext.current
    val certificationRepository = remember { CertificationFactory.createCertificationRepository(context) }
    val viewModelFactory = remember { CertificationViewModelFactory(certificationRepository) }
    val viewModel: AjukanSertifikasiViewModel = viewModel(factory = viewModelFactory)
    
    val state by viewModel.state.collectAsState()
    
    // Form fields state
    var fileCSR by remember { mutableStateOf<String?>(null) }
    var fileLingkungan by remember { mutableStateOf<String?>(null) }
    var fileImplementasi by remember { mutableStateOf<String?>(null) }
    var filePernyataan by remember { mutableStateOf<String?>(null) }

    val isBothFilesUploaded = fileCSR != null && fileLingkungan != null && fileImplementasi != null && filePernyataan != null
    
    // Function to submit certification with data from navigation
    fun submitCertificationWithData() {
        if (certificationName != null && certificationDescription != null && 
            certificationCredentialBody != null && certificationBenefits != null && 
            certificationCost != null) {
            
            // Set all form fields with data from navigation
            viewModel.updateFormField("name", certificationName)
            viewModel.updateFormField("description", certificationDescription)
            viewModel.updateFormField("credentialBody", certificationCredentialBody)
            viewModel.updateFormField("benefits", certificationBenefits)
            viewModel.updateFormField("cost", certificationCost)
            
            // Add hardcoded supporting documents as proper JSON array
            viewModel.clearSupportingDocuments()
            
            // Add each document separately (ViewModel should handle JSON array creation)
            viewModel.addSupportingDocument("https://example.com/csr_2024.pdf", "CSR_2024.pdf")
            viewModel.addSupportingDocument("https://example.com/kebijakan.pdf", "Kebijakan_Enviro.pdf")
            
            // Submit the certification
            viewModel.submitCertification()
        }
    }
    
    // Show success dialog when submission is successful
    if (state.submissionSuccess) {
        AlertDialog(
            onDismissRequest = { 
                viewModel.clearSubmissionStatus()
                navController.navigate("berhasil")
            },
            title = { Text("Success") },
            text = { Text(state.submissionMessage ?: "Certification application submitted successfully!") },
            confirmButton = {
                Button(
                    onClick = { 
                        viewModel.clearSubmissionStatus()
                        navController.navigate("berhasil")
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
    
    // Show error dialog when there's an error
    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(error) },
            confirmButton = {
                Button(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
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
            
            // Show selected certification info
            certificationName?.let { name ->
                Text(
                    text = "Sertifikasi Dipilih:",
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A4218),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                certificationCost?.let { cost ->
                    Text(
                        text = "Biaya: Rp ${String.format("%,.0f", cost.toDouble())}",
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A4218)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            Text(
                text = "Unggah Dokumen Pendukung",
                fontSize = 22.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 2.dp)
            )
            Text(
                text = "Unggah dokumen pendukung untuk melanjutkan pengajuan sertifikasi",
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
                placeholder = "Laporan Implementasi",
                onUploadClick = { fileImplementasi = "Project_TreePlanting.pdf" }, // Simulasi upload
                onDelete = { fileImplementasi = null },
                modifier = Modifier.padding(top=20.dp)
            )

            UploadField(
                fileName = filePernyataan,
                placeholder = "Surat Pernyataan",
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

            Spacer(modifier = Modifier.height(80.dp))

            Button(
                onClick = { 
                    if (isBothFilesUploaded) {
                        submitCertificationWithData()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 1.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    if (isBothFilesUploaded) Color(0xFF27361F) else Color(0xFF989898)
                ),
                enabled = isBothFilesUploaded && !state.isSubmitting
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Ajukan Sertifikasi",
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
    DokumenOne(
        navController = rememberNavController(),
        certificationName = "Sertifikat CSR ISO 26000",
        certificationCost = "1500000.0"
    )
}