package com.example.tumbuhnyata.ui.eventcsr

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import android.net.Uri
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.*
import com.google.gson.Gson
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CsrSubmissionScreen(navController: NavController) {
    val viewModel: CsrSubmissionViewModel = viewModel()
    var step by remember { mutableStateOf(1) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with back button and step indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                Image(
                    painter = painterResource(id = R.drawable.btn_back),
                    contentDescription = "Kembali",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController.navigateUp() }
                )
                
                Text(
                    text = "Ajukan CSR",
                    fontSize = 20.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Text(
                    text = "Langkah $step/4",
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily,
                    color = Color.Gray
                )
            }
        }

        // Content based on current step
        AnimatedContent(targetState = step, label = "CSR Steps") { currentStep ->
            when (currentStep) {
                1 -> StepOne(
                    programName = viewModel.programName.value,
                    selectedCategory = viewModel.category.value,
                    onProgramNameChange = { viewModel.programName.value = it },
                    onCategoryChange = { viewModel.category.value = it },
                    onNext = { step++ }
                )
                2 -> StepTwo(
                    location = viewModel.location.value,
                    partnerName = viewModel.partnerName.value,
                    startDate = viewModel.startDate.value,
                    endDate = viewModel.endDate.value,
                    budget = viewModel.budget.value,
                    onLocationChange = { viewModel.location.value = it },
                    onPartnerNameChange = { viewModel.partnerName.value = it },
                    onStartDateChange = { viewModel.startDate.value = it },
                    onEndDateChange = { viewModel.endDate.value = it },
                    onBudgetChange = { viewModel.budget.value = it },
                    onNext = { step++ }
                )
                3 -> StepThree { step++ }
                4 -> StepFour(navController = navController) { 
                    // Create CSR data object
                    val csrData = CsrData(
                        programName = viewModel.programName.value,
                        category = viewModel.category.value,
                        startDate = viewModel.startDate.value,
                        endDate = viewModel.endDate.value,
                        location = viewModel.location.value,
                        partnerName = viewModel.partnerName.value,
                        budget = viewModel.budget.value
                    )
                    // Convert to JSON and encode for URL
                    val csrDataJson = Uri.encode(Gson().toJson(csrData))
                    // Navigate to verification screen with data
                    navController.navigate("csr_verification/$csrDataJson")
                }
            }
        }
    }
}

@Composable
fun StepOne(
    programName: String,
    selectedCategory: String,
    onProgramNameChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onNext: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    
    val categories = listOf("Lingkungan")
    val viewModel: CsrSubmissionViewModel = viewModel()
    val isFormValid = viewModel.isFormStepOneValid(description)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Program Name
        OutlinedTextField(
            value = programName,
            onValueChange = onProgramNameChange,
            label = { Text("Nama Program", fontFamily = PoppinsFontFamily) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color(0xFF3A5A40)
            )
        )
        
        Text(
            text = "*Tuliskan nama kegiatan dengan pendek dan ringkas",
            fontSize = 12.sp,
            color = Color.Gray,
            fontFamily = PoppinsFontFamily,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Category Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = { },
                label = { Text("Kategori", fontFamily = PoppinsFontFamily) },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCategoryDropdown = true },
                trailingIcon = {
                    IconButton(
                        onClick = { showCategoryDropdown = true }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dropdown),
                            contentDescription = "Dropdown",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF3A5A40)
                )
            )

            DropdownMenu(
                expanded = showCategoryDropdown,
                onDismissRequest = { showCategoryDropdown = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { 
                            Text(
                                text = category,
                                fontFamily = PoppinsFontFamily,
                                fontSize = 16.sp
                            ) 
                        },
                        onClick = {
                            onCategoryChange(category)
                            showCategoryDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        OutlinedTextField(
            value = description,
            onValueChange = { 
                if (it.length <= 2000) {
                    description = it 
                }
            },
            label = { Text("Deskripsi", fontFamily = PoppinsFontFamily) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color(0xFF3A5A40)
            ),
            supportingText = {
                Text(
                    text = "${description.length}/2000",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    fontFamily = PoppinsFontFamily
                )
            }
        )
        
        Text(
            text = "*Deskripsikan secara singkat program anda dengan menuliskan tujuan dan sasaran program secara singkat",
            fontSize = 12.sp,
            color = Color.Gray,
            fontFamily = PoppinsFontFamily,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Next Button
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) Color(0xFF27361F) else Color.Gray,
                contentColor = Color.White
            ),
            enabled = isFormValid
        ) {
            Text(
                text = "Selanjutnya",
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTwo(
    location: String,
    partnerName: String,
    startDate: String,
    endDate: String,
    budget: String,
    onLocationChange: (String) -> Unit,
    onPartnerNameChange: (String) -> Unit,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onBudgetChange: (String) -> Unit,
    onNext: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var isSelectingStartDate by remember { mutableStateOf(true) }

    val datePickerState = rememberDatePickerState()
    val viewModel: CsrSubmissionViewModel = viewModel()
    val isFormValid = viewModel.isFormStepTwoValid()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Location Search
        OutlinedTextField(
            value = location,
            onValueChange = onLocationChange,
            label = { Text("Lokasi Pelaksanaan", fontFamily = PoppinsFontFamily) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_event_loc1),
                    contentDescription = "Location",
                    modifier = Modifier.size(24.dp)
                )
            },
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color(0xFF3A5A40)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Partner Name
        OutlinedTextField(
            value = partnerName,
            onValueChange = onPartnerNameChange,
            label = { Text("Nama Mitra (Jika Ada)", fontFamily = PoppinsFontFamily) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color(0xFF3A5A40)
            )
        )

        Text(
            text = "*Apabila nama mitra kosong, maka kami akan mencarikan mitra untuk program CSR anda",
            fontSize = 12.sp,
            color = Color.Gray,
            fontFamily = PoppinsFontFamily,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Program Period
        Text(
            text = "Periode Program",
            fontSize = 16.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Start Date
            OutlinedTextField(
                value = startDate,
                onValueChange = { },
                label = { Text("Mulai", fontFamily = PoppinsFontFamily) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                readOnly = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isSelectingStartDate = true
                            showDatePicker = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_event_calendar1),
                            contentDescription = "Calendar",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF3A5A40)
                )
            )

            // End Date
            OutlinedTextField(
                value = endDate,
                onValueChange = { },
                label = { Text("Berakhir", fontFamily = PoppinsFontFamily) },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                readOnly = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isSelectingStartDate = false
                            showDatePicker = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_event_calendar1),
                            contentDescription = "Calendar",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF3A5A40)
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Budget with number validation
        OutlinedTextField(
            value = budget,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    onBudgetChange(newValue)
                }
            },
            label = { Text("Anggaran", fontFamily = PoppinsFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Text(
                    text = "Rp",
                    modifier = Modifier.padding(start = 16.dp),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color(0xFF3A5A40)
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        // Next Button
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) Color(0xFF27361F) else Color.Gray,
                contentColor = Color.White
            ),
            enabled = isFormValid
        ) {
            Text(
                text = "Selanjutnya",
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))
                            if (isSelectingStartDate) {
                                onStartDateChange(formattedDate)
                            } else {
                                onEndDateChange(formattedDate)
                            }
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

@Composable
fun StepThree(onNext: () -> Unit) {
    var proposalFile by remember { mutableStateOf<String?>(null) }
    var legalityFile by remember { mutableStateOf<String?>(null) }
    
    val isFormValid = proposalFile != null && legalityFile != null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Unggah Dokumen Pendukung",
            fontSize = 20.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Unggah dokumen pendukung untuk melanjutkan pengajuan CSR perusahaan anda",
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = PoppinsFontFamily,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Proposal Upload
        Text(
            text = "Proposal Rancangan",
            fontSize = 16.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = proposalFile == null) { 
                    proposalFile = "Proposal Rancangan.pdf"
                }
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (proposalFile == null) R.drawable.ic_upload 
                            else R.drawable.ic_doc
                        ),
                        contentDescription = "Upload",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Gray
                    )
                    
                    Text(
                        text = proposalFile ?: "Proposal Rancangan",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = PoppinsFontFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (proposalFile != null) {
                    IconButton(
                        onClick = { proposalFile = null }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Delete",
                            tint = Color(0xFFE74C3C),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Legality Upload
        Text(
            text = "Legalitas Izin",
            fontSize = 16.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = legalityFile == null) { 
                    legalityFile = "Legalitas Izin.pdf"
                }
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (legalityFile == null) R.drawable.ic_upload 
                            else R.drawable.ic_doc
                        ),
                        contentDescription = "Upload",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Gray
                    )
                    
                    Text(
                        text = legalityFile ?: "Legalitas Izin",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = PoppinsFontFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (legalityFile != null) {
                    IconButton(
                        onClick = { legalityFile = null }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Delete",
                            tint = Color(0xFFE74C3C),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Next Button
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) Color(0xFF27361F) else Color.Gray,
                contentColor = Color.White
            ),
            enabled = isFormValid
        ) {
            Text(
                text = "Selanjutnya",
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StepFour(navController: NavController, onNext: () -> Unit) {
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Pernyataan",
            fontSize = 20.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF27361F),
                            uncheckedColor = Color.Gray
                        )
                    )
                    
                    Text(
                        text = "Semua data dan dokumen yang anda kirim akan digunakan untuk analisis tim TUMBUH NYATA",
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Submit Button
        Button(
            onClick = { navController.navigate("csr_verification") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isChecked) Color(0xFF27361F) else Color.Gray,
                contentColor = Color.White
            ),
            enabled = isChecked
        ) {
            Text(
                text = "Selanjutnya",
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun PreviewCsrSubmissionScreen() {
    val navController = rememberNavController()
    CsrSubmissionScreen(navController = navController)
}
