package com.example.tumbuhnyata.ui.eventcsr

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.local.entity.CsrDraftEntity
import com.example.tumbuhnyata.data.local.AppDatabase
import com.example.tumbuhnyata.data.repository.CsrDraftRepository
import com.example.tumbuhnyata.data.api.CsrApiService
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.viewmodel.CsrDraftViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.tumbuhnyata.data.model.CsrData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftListScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val csrApiService = remember { Retrofit.Builder().baseUrl("http://10.0.2.2:5000/").addConverterFactory(GsonConverterFactory.create()).build().create(
        CsrApiService::class.java) }
    val repository = remember { CsrDraftRepository(db.csrDraftDao(), csrApiService) }
    val viewModel: CsrDraftViewModel = viewModel(factory = CsrDraftViewModelFactory(repository))

    val drafts by viewModel.allDrafts.collectAsState()

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF27361F))
                            .clickable { navController.navigateUp() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                    Text(
                        text = "Daftar Draft CSR",
                        fontSize = 20.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            if (drafts.isEmpty()) {
                Text(
                    text = "Tidak ada draft yang tersimpan.",
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(drafts) { draft ->
                        DraftItem(draft = draft, navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun DraftItem(
    draft: CsrDraftEntity,
    navController: NavController,
    viewModel: CsrDraftViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle edit here */ },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = draft.programName,
                    fontSize = 18.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Kategori: ${draft.category}",
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.Gray
            )
            Text(
                text = "Lokasi: ${draft.location}",
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.Gray
            )
            Text(
                text = "Tanggal: ${draft.startDate} - ${draft.endDate}",
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        viewModel.submitDraftToApi(draft, onSuccess = { navController.navigate("csr_success") }, onError = { /* TODO: Show error message */ })
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A5A40))
                ) {
                    Text("Ajukan", color = Color.White)
                }
                IconButton(onClick = { viewModel.deleteDraft(draft) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE74C3C))
                }
            }
        }
    }
}

// Extension function to convert CsrDraftEntity to CsrData
fun CsrDraftEntity.toCsrData(): CsrData {
    return CsrData(
        programName = this.programName,
        category = this.category,
        description = this.description,
        startDate = this.startDate,
        endDate = this.endDate,
        location = this.location,
        partnerName = this.partnerName,
        budget = this.budget
    )
}

// ViewModel Factory for CsrDraftViewModel
class CsrDraftViewModelFactory(private val repository: CsrDraftRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CsrDraftViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CsrDraftViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDraftListScreen() {
    val navController = rememberNavController()
    // For preview purposes, we need to mock the ViewModel or provide a dummy one.
    // This preview will not fully function without a real ViewModel and database setup.
    DraftListScreen(navController = navController)
}