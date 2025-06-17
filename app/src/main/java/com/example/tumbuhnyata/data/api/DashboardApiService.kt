package com.example.tumbuhnyata.data.api

import com.example.tumbuhnyata.data.model.DashboardDataResponse // Model baru untuk endpoint /dashboard
import com.example.tumbuhnyata.data.model.CsrReportModel // Jika ada endpoint lain yang mengembalikan list ini
import com.example.tumbuhnyata.data.model.KpiDetailResponse // Model untuk KPI detail
import com.example.tumbuhnyata.data.model.CsrSubmissionsResponse // Added for the new submissions response type
import com.example.tumbuhnyata.data.model.UploadResponse // Model for upload response
import com.example.tumbuhnyata.data.model.CreateSubmissionRequest
import com.example.tumbuhnyata.data.model.CreateSubmissionResponse
import com.example.tumbuhnyata.data.model.UpdateSubmissionRequest
import com.example.tumbuhnyata.data.model.UpdateSubmissionResponse
import com.example.tumbuhnyata.data.model.DeleteSubmissionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface DashboardApiService {

    @GET("carbon-submissions/dashboard")
    suspend fun getDashboardData(
 
        @Query("company_id") companyId: Int? = null,
        @Query("year") year: Int? = null
    ): Response<DashboardDataResponse>

    @GET("dashboard")
    suspend fun getDashboardDataNoPrefIx(
        @Query("company_id") companyId: Int? = null,
        @Query("year") year: Int? = null
    ): Response<DashboardDataResponse>

    @GET("carbon-submissions/kpi/{kpiType}")
    suspend fun getKpiDetail(
        @Path("kpiType") kpiType: String,
        @Query("company_id") companyId: String? = null,
        @Query("year") year: Int
    ): Response<KpiDetailResponse>

    @GET("carbon-submissions/submissions")
    suspend fun getAllSubmissions(
        @Query("company_id") companyId: String? = null,
        @Query("year") year: Int? = null,
        @Query("month") month: Int? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<CsrSubmissionsResponse>

    @GET("carbon-submissions/analytics")
    suspend fun getAnalytics(
        @Query("company_id") companyId: Int? = null,
        @Query("year") year: Int? = null
    ): Response<DashboardDataResponse>

    @Multipart
    @POST("carbon-submissions/upload-csv")
    suspend fun uploadCsvFile(
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>
    
    // INDIVIDUAL SUBMISSION MANAGEMENT ENDPOINTS
    
    @POST("carbon-submissions/submissions")
    suspend fun createSubmission(
        @Body request: CreateSubmissionRequest
    ): Response<CreateSubmissionResponse>
    
    @PUT("carbon-submissions/{id}")
    suspend fun updateSubmission(
        @Path("id") id: Int,
        @Body request: UpdateSubmissionRequest
    ): Response<UpdateSubmissionResponse>
    
    @DELETE("carbon-submissions/{id}")
    suspend fun deleteSubmission(
        @Path("id") id: Int
    ): Response<DeleteSubmissionResponse>
} 