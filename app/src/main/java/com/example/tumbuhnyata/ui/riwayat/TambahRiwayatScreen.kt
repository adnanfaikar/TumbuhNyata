package com.example.tumbuhnyata.ui.riwayat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tumbuhnyata.ui.component.poppins
import com.example.tumbuhnyata.viewmodel.TambahRiwayatViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahRiwayatScreen(
    navController: NavController,
    viewModel: TambahRiwayatViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    // Collect state
    val programName by viewModel.programName.collectAsState()
    val mitra by viewModel.mitra.collectAsState()
    val status by viewModel.status.collectAsState()
    val kategori by viewModel.kategori.collectAsState()
    val lokasi by viewModel.lokasi.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    val deskripsi by viewModel.deskripsi.collectAsState()
    val budget by viewModel.budget.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    
    // Dropdown states
    var statusExpanded by remember { mutableStateOf(false) }
    var kategoriExpanded by remember { mutableStateOf(false) }
    
    // Date picker states
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    
    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(paddingValues)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2C3E1F))
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Tambah CSR",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppins,
                    color = Color.Black
                )
            }

            // Form Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nama Program
                Column {
                    Text(
                        text = "Nama Program",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppins,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = programName,
                        onValueChange = viewModel::updateProgramName,
                        placeholder = { Text("Nama Kegiatan", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C3E1F),
                            unfocusedBorderColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Text(
                        text = "*Tuliskan nama kegiatan dengan pendek dan ringkas",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = poppins,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Mitra
                Column {
                    Text(
                        text = "Mitra",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppins,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = mitra,
                        onValueChange = viewModel::updateMitra,
                        placeholder = { Text("Nama Mitra", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C3E1F),
                            unfocusedBorderColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                // Status
                Column {
                    Text(
                        text = "Status",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppins,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = statusExpanded,
                        onExpandedChange = { statusExpanded = !statusExpanded }
                    ) {
                        OutlinedTextField(
                            value = status,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Status", color = Color.Gray) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Dropdown"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2C3E1F),
                                unfocusedBorderColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = statusExpanded,
                            onDismissRequest = { statusExpanded = false }
                        ) {
                            viewModel.statusOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        viewModel.updateStatus(option)
                                        statusExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Kategori Program
                Column {
                    Text(
                        text = "Kategori Program",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppins,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = kategoriExpanded,
                        onExpandedChange = { kategoriExpanded = !kategoriExpanded }
                    ) {
                        OutlinedTextField(
                            value = kategori,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Kategori", color = Color.Gray) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Dropdown"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2C3E1F),
                                unfocusedBorderColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = kategoriExpanded,
                            onDismissRequest = { kategoriExpanded = false }
                        ) {
                            viewModel.kategoriOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        viewModel.updateKategori(option)
                                        kategoriExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Lokasi
                Column {
                    Text(
                        text = "Lokasi",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppins,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = lokasi,
                        onValueChange = viewModel::updateLokasi,
                        placeholder = { Text("Lokasi", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C3E1F),
                            unfocusedBorderColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                // Periode waktu
                Column {
                    Text(
                        text = "Periode",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppins,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                            "$startDate - $endDate"
                        } else {
                            ""
                        },
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Periode waktu", color = Color.Gray) },
                        trailingIcon = {
                            IconButton(onClick = { showStartDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Pilih Tanggal"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C3E1F),
                            unfocusedBorderColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                // Deskripsi
                Column {
                    Text(
                        text = "Deskripsi",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppins,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = deskripsi,
                        onValueChange = viewModel::updateDeskripsi,
                        placeholder = { Text("Deskripsi program", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C3E1F),
                            unfocusedBorderColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 4
                    )
                }

                // Budget
                Column {
                    Text(
                        text = "Budget",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppins,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = budget,
                        onValueChange = viewModel::updateBudget,
                        placeholder = { Text("Masukkan budget", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C3E1F),
                            unfocusedBorderColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Submit Button
                Button(
                    onClick = {
                        viewModel.submitForm {
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2C3E1F)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "Kirim",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppins,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Show success/error messages
    LaunchedEffect(successMessage) {
        successMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long
            )
            viewModel.clearSuccessMessage()
        }
    }
    
    LaunchedEffect(error) {
        error?.let { errorMessage ->
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    // Date Picker for start date
    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Date(millis)
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            viewModel.updateStartDate(formatter.format(date))
                        }
                        showStartDatePicker = false
                        showEndDatePicker = true
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Date Picker for end date
    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Date(millis)
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            viewModel.updateEndDate(formatter.format(date))
                        }
                        showEndDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
} 