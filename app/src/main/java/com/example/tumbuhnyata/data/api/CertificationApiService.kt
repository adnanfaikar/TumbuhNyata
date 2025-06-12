package com.example.tumbuhnyata.data.api

import com.example.tumbuhnyata.data.model.CreateCertificationRequest
import com.example.tumbuhnyata.data.model.CreateCertificationResponse
import com.example.tumbuhnyata.data.model.CertificationListResponse
import com.example.tumbuhnyata.data.model.CertificationDetailResponse
import com.example.tumbuhnyata.data.model.UpdateCertificationStatusRequest
import com.example.tumbuhnyata.data.model.UpdateStatusResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service for Certification endpoints
 * Based on backend controller: certificationsController
 */
interface CertificationApiService {

    /**
     * Get all certification applications for current user
     * GET /certifications
     */
    @GET("certifications")
    suspend fun getUserCertifications(
        @Query("user_id") userId: Int? = 1 // Hardcode user_id = 1 temporarily
    ): Response<CertificationListResponse>

    /**
     * Get certification detail by ID
     * GET /certifications/{id}
     */
    @GET("certifications/{id}")
    suspend fun getCertificationById(
        @Path("id") certificationId: Int
    ): Response<CertificationDetailResponse>

    /**
     * Submit new certification application
     * POST /certifications/apply
     */
    @POST("certifications/apply")
    suspend fun applyCertification(
        @Body request: CreateCertificationRequest
    ): Response<CreateCertificationResponse>

    /**
     * Update certification status (admin only)
     * PUT /certifications/{id}/status
     */
    @PUT("certifications/{id}/status")
    suspend fun updateCertificationStatus(
        @Path("id") certificationId: Int,
        @Body request: UpdateCertificationStatusRequest
    ): Response<UpdateStatusResponse>
} 