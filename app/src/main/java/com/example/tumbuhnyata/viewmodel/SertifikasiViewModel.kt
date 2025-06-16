package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.local.entity.CertificationEntity
import com.example.tumbuhnyata.data.repository.CertificationRepository
import com.example.tumbuhnyata.data.repository.CertificationResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SertifikasiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val certificationList: List<CertificationEntity> = emptyList(),
    val syncStatus: String? = null
)

class SertifikasiViewModel(
    private val certificationRepository: CertificationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SertifikasiState())
    val state: StateFlow<SertifikasiState> = _state.asStateFlow()

    init {
        loadCertifications()
        startPeriodicSync()
    }

    private fun loadCertifications() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                
                certificationRepository.getAllCertifications().collect { resource ->
                    when (resource) {
                        is CertificationResource.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = null,
                                certificationList = resource.data ?: emptyList()
                            )
                        }
                        is CertificationResource.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = resource.message
                            )
                        }
                        is CertificationResource.Loading -> {
                            _state.value = _state.value.copy(isLoading = true)
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "An error occurred"
                )
            }
        }
    }

    fun refreshCertifications() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(syncStatus = "Syncing...")
                certificationRepository.refreshCertificationsCache()
                _state.value = _state.value.copy(syncStatus = "Sync complete")
            } catch (e: Exception) {
                _state.value = _state.value.copy(syncStatus = "Sync failed: ${e.message}")
            }
        }
    }

    fun getCertificationsByStatus(status: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                
                certificationRepository.getCertificationsByStatus(status).collect { resource ->
                    when (resource) {
                        is CertificationResource.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = null,
                                certificationList = resource.data ?: emptyList()
                            )
                        }
                        is CertificationResource.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = resource.message
                            )
                        }
                        is CertificationResource.Loading -> {
                            _state.value = _state.value.copy(isLoading = true)
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "An error occurred"
                )
            }
        }
    }

    private fun startPeriodicSync() {
        viewModelScope.launch {
            try {
                certificationRepository.startPeriodicSync()
            } catch (e: Exception) {
                println("SertifikasiViewModel: Error starting periodic sync: ${e.message}")
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    fun clearSyncStatus() {
        _state.value = _state.value.copy(syncStatus = null)
    }
}