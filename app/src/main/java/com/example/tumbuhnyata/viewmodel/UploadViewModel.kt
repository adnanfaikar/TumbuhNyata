package com.example.tumbuhnyata.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.model.UploadResponse
import com.example.tumbuhnyata.data.repository.UploadRepository
import com.example.tumbuhnyata.data.repository.DashboardRepository
import com.example.tumbuhnyata.data.util.Resource
import com.example.tumbuhnyata.data.network.RetrofitInstance
import com.example.tumbuhnyata.data.local.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UploadState(
    val isLoading: Boolean = false,
    val selectedFileUri: Uri? = null,
    val selectedFileName: String? = null,
    val uploadResult: Resource<UploadResponse>? = null,
    val isUploadEnabled: Boolean = false
)

class UploadViewModel(application: Application) : AndroidViewModel(application) {

    private val _uploadState = MutableStateFlow(UploadState())
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    // Manual DI for Repository
    private val uploadRepository: UploadRepository
    private val dashboardRepository: DashboardRepository

    init {
        val dashboardApi = RetrofitInstance.dashboardApi
        val dashboardDao = AppDatabase.getDatabase(application).dashboardDao()
        
        // Create dashboard repository first
        dashboardRepository = DashboardRepository(dashboardApi, dashboardDao, application.applicationContext)
        
        // Create upload repository with dashboard repository for integration
        uploadRepository = UploadRepository(dashboardApi, application.applicationContext, dashboardRepository)
    }

    /**
     * Sets the selected file for upload
     */
    fun selectFile(uri: Uri, fileName: String) {
        _uploadState.value = _uploadState.value.copy(
            selectedFileUri = uri,
            selectedFileName = fileName,
            isUploadEnabled = true,
            uploadResult = null // Clear previous results
        )
    }

    /**
     * Clears the selected file
     */
    fun clearSelectedFile() {
        _uploadState.value = _uploadState.value.copy(
            selectedFileUri = null,
            selectedFileName = null,
            isUploadEnabled = false,
            uploadResult = null
        )
    }

    /**
     * Uploads the selected file
     */
    fun uploadFile() {
        val fileUri = _uploadState.value.selectedFileUri
        if (fileUri == null) {
            _uploadState.value = _uploadState.value.copy(
                uploadResult = Resource.Error("No file selected")
            )
            return
        }

        viewModelScope.launch {
            _uploadState.value = _uploadState.value.copy(
                isLoading = true,
                uploadResult = null
            )

            val result = uploadRepository.uploadCsvFile(fileUri)
            
            _uploadState.value = _uploadState.value.copy(
                isLoading = false,
                uploadResult = result
            )
        }
    }

    /**
     * Clears the upload result (for dismissing error messages)
     */
    fun clearUploadResult() {
        _uploadState.value = _uploadState.value.copy(
            uploadResult = null
        )
    }
} 