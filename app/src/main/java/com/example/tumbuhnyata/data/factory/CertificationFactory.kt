package com.example.tumbuhnyata.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tumbuhnyata.data.api.CertificationApiService
import com.example.tumbuhnyata.data.local.AppDatabase
import com.example.tumbuhnyata.data.local.dao.CertificationDao
import com.example.tumbuhnyata.data.network.RetrofitInstance
import com.example.tumbuhnyata.data.repository.CertificationRepository
import com.example.tumbuhnyata.viewmodel.AjukanSertifikasiViewModel
import com.example.tumbuhnyata.viewmodel.SertifikasiViewModel

/**
 * Factory for creating CertificationRepository with proper dependencies
 */
object CertificationFactory {
    
    fun createCertificationRepository(context: Context): CertificationRepository {
        val database = AppDatabase.getDatabase(context)
        val certificationDao = database.certificationDao()
        // TEMPORARY: Use no-auth API like dashboard to bypass token requirement
        val certificationApiService = RetrofitInstance.certificationApiNoAuth
        
        return CertificationRepository(
            certificationApiService = certificationApiService,
            certificationDao = certificationDao,
            context = context
        )
    }
}

/**
 * ViewModelFactory for Certification ViewModels
 */
class CertificationViewModelFactory(
    private val certificationRepository: CertificationRepository
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SertifikasiViewModel::class.java) -> {
                SertifikasiViewModel(certificationRepository) as T
            }
            modelClass.isAssignableFrom(AjukanSertifikasiViewModel::class.java) -> {
                AjukanSertifikasiViewModel(certificationRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
} 