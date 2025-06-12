package com.example.tumbuhnyata.data.repository

import android.content.Context
import android.net.Uri
import com.example.tumbuhnyata.data.api.DashboardApiService
import com.example.tumbuhnyata.data.model.UploadResponse
import com.example.tumbuhnyata.data.util.Resource
import com.example.tumbuhnyata.util.NetworkConnectivityUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import com.example.tumbuhnyata.data.mapper.createOfflineEntity
import java.util.Calendar

class UploadRepository(
    private val dashboardApiService: DashboardApiService,
    private val context: Context,
    private val dashboardRepository: DashboardRepository? = null // Optional for dashboard refresh
) {

    /**
     * Uploads a CSV file to the backend OR stores offline if no connection
     * ENHANCED: Better integration with new sync system and improved user feedback
     * @param fileUri The URI of the selected file
     * @return Resource wrapper containing upload result
     */
    suspend fun uploadCsvFile(fileUri: Uri): Resource<UploadResponse> {
        return withContext(Dispatchers.IO) {
            try {
                println("UploadRepository: === STARTING CSV UPLOAD PROCESS ===")
                
                // Convert URI to File
                val file = createFileFromUri(fileUri)
                if (file == null) {
                    println("UploadRepository: Failed to create file from URI")
                    return@withContext Resource.Error("Failed to process selected file")
                }

                // Validate file extension
                if (!file.name.endsWith(".csv", ignoreCase = true)) {
                    file.delete() // Clean up
                    println("UploadRepository: Invalid file extension: ${file.name}")
                    return@withContext Resource.Error("Please select a CSV file (.csv extension required)")
                }

                // Check network status and choose strategy
                val isOnline = NetworkConnectivityUtil.isOnline(context)
                println("UploadRepository: Network status: ${if (isOnline) "ONLINE" else "OFFLINE"}")

                if (isOnline) {
                    // ONLINE: Parse CSV locally first, then upload to server
                    uploadOnlineWithLocalParsing(file)
                } else {
                    // OFFLINE: Parse and store locally for later sync
                    uploadOfflineWithLocalStorage(file)
                }
            } catch (e: Exception) {
                println("UploadRepository: Critical error in upload process: ${e.message}")
                e.printStackTrace()
                Resource.Error("Upload failed: ${e.message}")
            }
        }
    }

    /**
     * Online upload strategy: Parse locally first for validation, then upload to server
     * This provides immediate feedback and ensures data consistency
     */
    private suspend fun uploadOnlineWithLocalParsing(file: File): Resource<UploadResponse> {
        return try {
            println("UploadRepository: ONLINE MODE - Using hybrid approach (local parsing + server upload)")
            
            // Step 1: Parse CSV locally for immediate validation and feedback
            val csvEntries = parseCsvFile(file)
            if (csvEntries.isEmpty()) {
                file.delete()
                return Resource.Error("No valid data found in CSV file")
            }
            
            println("UploadRepository: Local parsing successful - ${csvEntries.size} entries validated")
            
            // Step 2: Upload original file to server (backend will parse again for consistency)
            val serverResult = uploadToServer(file)
            
            when (serverResult) {
                is Resource.Success -> {
                    println("UploadRepository: Server upload successful, triggering cache refresh...")
                    // Trigger dashboard cache refresh to get the new data
                    try {
                        dashboardRepository?.refreshSubmissionsCache(null, null)
                        println("UploadRepository: Cache refresh completed")
                    } catch (e: Exception) {
                        println("UploadRepository: Cache refresh failed: ${e.message}")
                        // Don't fail the whole operation for cache refresh failure
                    }
                    serverResult
                }
                is Resource.Error -> {
                    println("UploadRepository: Server upload failed, falling back to offline storage...")
                    // Server failed, fall back to offline storage
                    storeLocallyForSync(csvEntries, file.name)
                }
                else -> serverResult
            }
            
        } catch (e: Exception) {
            file.delete()
            println("UploadRepository: Error in online upload process: ${e.message}")
            e.printStackTrace()
            Resource.Error("Online upload failed: ${e.message}")
        }
    }
    
    /**
     * Offline upload strategy: Parse and store locally for later sync
     */
    private suspend fun uploadOfflineWithLocalStorage(file: File): Resource<UploadResponse> {
        return try {
            println("UploadRepository: OFFLINE MODE - Storing data locally for later sync")
            
            // Parse CSV content
            val csvEntries = parseCsvFile(file)
            file.delete() // Clean up file after parsing
            
            if (csvEntries.isEmpty()) {
                return Resource.Error("No valid data found in CSV file")
            }

            // Store for later sync
            storeLocallyForSync(csvEntries, file.name)
            
        } catch (e: Exception) {
            file.delete()
            println("UploadRepository: Error in offline upload process: ${e.message}")
            e.printStackTrace()
            Resource.Error("Offline storage failed: ${e.message}")
        }
    }
    
    /**
     * Stores parsed CSV entries locally for later sync
     */
    private suspend fun storeLocallyForSync(csvEntries: List<com.example.tumbuhnyata.data.local.entity.CsrReportEntity>, fileName: String): Resource<UploadResponse> {
        return try {
            dashboardRepository?.let { repo ->
                val dashboardDao = repo.getDashboardDao()
                var savedCount = 0
                var failedCount = 0
                val saveErrors = mutableListOf<String>()
                
                println("UploadRepository: Storing ${csvEntries.size} entries locally...")
                
                csvEntries.forEachIndexed { index, entry ->
                    try {
                        println("UploadRepository: [${index + 1}/${csvEntries.size}] Attempting to save:")
                        println("  - Document: '${entry.documentName}'")
                        println("  - Company ID: ${entry.companyId}")
                        println("  - Year: ${entry.year}, Month: ${entry.month}")
                        println("  - Carbon Value: ${entry.carbonValue}")
                        println("  - Local ID: ${entry.localId}")
                        println("  - isLocalOnly: ${entry.isLocalOnly}, isSynced: ${entry.isSynced}")
                        
                        val entityId = dashboardDao.insertReport(entry)
                        if (entityId > 0) {
                            savedCount++
                            println("UploadRepository: [${index + 1}/${csvEntries.size}] ✓ Saved with ID: $entityId")
                        } else {
                            failedCount++
                            saveErrors.add("Failed to save entry ${index + 1}: ${entry.documentName} (returned ID: $entityId)")
                            println("UploadRepository: [${index + 1}/${csvEntries.size}] ✗ Failed to save: ${entry.documentName} (returned ID: $entityId)")
                        }
                    } catch (e: Exception) {
                        failedCount++
                        saveErrors.add("Exception saving entry ${index + 1}: ${e.message}")
                        println("UploadRepository: [${index + 1}/${csvEntries.size}] ✗ Exception: ${e.message}")
                        e.printStackTrace()
                    }
                }
                
                println("UploadRepository: === LOCAL STORAGE SUMMARY ===")
                println("UploadRepository: Total Entries: ${csvEntries.size}")
                println("UploadRepository: Successfully Saved: $savedCount")
                println("UploadRepository: Failed: $failedCount")
                println("UploadRepository: ================================")
                
                if (savedCount > 0) {
                    val message = if (failedCount > 0) {
                        "Partial success: $savedCount of ${csvEntries.size} records saved offline. Will sync when online."
                    } else {
                        "All $savedCount records saved offline successfully. Will sync when online."
                    }
                    
                    // Create success response
                    val mockResponse = UploadResponse(
                        success = true,
                        message = message,
                        submissionId = null,
                        processedRecords = savedCount,
                        errors = if (failedCount > 0) saveErrors else null
                    )
                    Resource.Success(mockResponse)
                } else {
                    Resource.Error("Failed to save any records locally: ${saveErrors.firstOrNull() ?: "Unknown error"}")
                }
                
            } ?: Resource.Error("Database not available for offline storage")
            
        } catch (e: Exception) {
            println("UploadRepository: Critical error in local storage: ${e.message}")
            e.printStackTrace()
            Resource.Error("Local storage failed: ${e.message}")
        }
    }

    /**
     * Uploads file to server (online mode)
     */
    private suspend fun uploadToServer(file: File): Resource<UploadResponse> {
        return try {
            // Create multipart body
            val requestFile = file.asRequestBody("text/csv".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("csvFile", file.name, requestFile)

            // Make API call
            val response = dashboardApiService.uploadCsvFile(multipartBody)

            // Clean up temporary file
            file.delete()

            if (response.isSuccessful) {
                val uploadResponse = response.body()
                if (uploadResponse != null) {
                    if (uploadResponse.success) {
                        // Upload berhasil - trigger refresh dashboard data
                        try {
                            dashboardRepository?.let { repo ->
                                println("UploadRepository: Upload successful, triggering dashboard refresh...")
                                // Refresh submissions cache dengan data terbaru
                                repo.refreshSubmissionsCache(null, null)
                                println("UploadRepository: Dashboard cache refreshed after upload")
                            }
                        } catch (e: Exception) {
                            println("UploadRepository: Failed to refresh dashboard after upload: ${e.message}")
                            // Don't fail the upload because of refresh failure
                        }
                        Resource.Success(uploadResponse)
                    } else {
                        Resource.Error(uploadResponse.message ?: "Upload failed")
                    }
                } else {
                    Resource.Error("No response from server")
                }
            } else {
                Resource.Error("Upload failed: ${response.message()}")
            }
        } catch (e: Exception) {
            file.delete() // Clean up on error
            Resource.Error("Upload failed: ${e.message}")
        }
    }

    /**
     * Parses CSV file content into CsrReportEntity list
     * FIXED: Improved multi-row parsing with comprehensive debugging
     */
    private suspend fun parseCsvFile(file: File): List<com.example.tumbuhnyata.data.local.entity.CsrReportEntity> {
        return withContext(Dispatchers.IO) {
            val entries = mutableListOf<com.example.tumbuhnyata.data.local.entity.CsrReportEntity>()
            
            try {
                println("UploadRepository: Starting CSV parsing for file: ${file.name}")
                println("UploadRepository: File size: ${file.length()} bytes")
                
                // Read file content with proper encoding detection
                val fileContent = file.readText(Charsets.UTF_8)
                println("UploadRepository: File content preview (first 300 chars): ${fileContent.take(300)}")
                
                // Split lines and filter empty ones
                val allLines = fileContent.split('\n', '\r').filter { it.trim().isNotEmpty() }
                println("UploadRepository: Total non-empty lines in file: ${allLines.size}")
                
                if (allLines.isEmpty()) {
                    println("UploadRepository: File is empty after splitting")
                    return@withContext emptyList()
                }
                
                // Show first few lines for debugging
                allLines.take(3).forEachIndexed { index, line ->
                    println("UploadRepository: Line ${index + 1}: '${line.trim()}'")
                }
                
                var processedDataLines = 0
                var skippedLines = 0
                var errorLines = 0
                
                // Process each line (skip header)
                allLines.forEachIndexed { index, line ->
                    val lineNumber = index + 1
                    val trimmedLine = line.trim()
                    
                    if (index == 0) {
                        println("UploadRepository: Skipping header line $lineNumber: '$trimmedLine'")
                        skippedLines++
                        return@forEachIndexed // Skip header
                    }
                    
                    if (trimmedLine.isEmpty()) {
                        println("UploadRepository: Skipping empty line $lineNumber")
                        skippedLines++
                        return@forEachIndexed
                    }
                    
                    println("UploadRepository: Processing data line $lineNumber: '${trimmedLine.take(150)}${if (trimmedLine.length > 150) "..." else ""}'")
                    
                    try {
                        val csvEntry = parseCsvLine(trimmedLine, file.name, lineNumber)
                        if (csvEntry != null) {
                            entries.add(csvEntry)
                            processedDataLines++
                            println("UploadRepository: ✓ Successfully parsed line $lineNumber -> Document: '${csvEntry.documentName}', Carbon: ${csvEntry.carbonValue}")
                        } else {
                            println("UploadRepository: ✗ Line $lineNumber resulted in null entry (validation failed)")
                            errorLines++
                        }
                    } catch (e: Exception) {
                        println("UploadRepository: ✗ Exception parsing line $lineNumber: ${e.message}")
                        e.printStackTrace()
                        errorLines++
                    }
                }
                
                println("UploadRepository: === CSV PARSING SUMMARY ===")
                println("UploadRepository: Total lines in file: ${allLines.size}")
                println("UploadRepository: Header lines skipped: 1")
                println("UploadRepository: Empty lines skipped: ${skippedLines - 1}") // -1 for header
                println("UploadRepository: Data lines processed successfully: $processedDataLines")
                println("UploadRepository: Data lines with errors: $errorLines")
                println("UploadRepository: Final parsed entries: ${entries.size}")
                println("UploadRepository: ================================")
                
                entries
                
            } catch (e: Exception) {
                println("UploadRepository: Critical error parsing CSV file: ${e.message}")
                e.printStackTrace()
                emptyList()
            }
        }
    }

    /**
     * Parses a single CSV line into CsrReportEntity
     * FIXED: Better CSV splitting and validation with comprehensive debugging
     */
    private fun parseCsvLine(line: String, fileName: String, lineNumber: Int): com.example.tumbuhnyata.data.local.entity.CsrReportEntity? {
        try {
            // Handle different CSV formats (quoted and unquoted)
            val columns = if (line.contains("\"")) {
                // Handle quoted CSV (e.g., "value1","value2","value with, comma")
                parseQuotedCsv(line)
            } else {
                // Simple comma split for unquoted CSV
                line.split(",").map { it.trim() }
            }
            
            println("UploadRepository: Line $lineNumber -> Parsed ${columns.size} columns: $columns")
            
            // Validate minimum required columns
            if (columns.size < 4) {
                println("UploadRepository: ✗ Line $lineNumber has insufficient columns (need at least 4: company_id,year,month,carbon_value, got ${columns.size})")
                return null
            }
            
            // Parse each field with comprehensive error handling
            val companyId = parseIntField(columns.getOrNull(0), "company_id", lineNumber) ?: 1
            val year = parseIntField(columns.getOrNull(1), "year", lineNumber) ?: Calendar.getInstance().get(Calendar.YEAR)
            val month = parseIntField(columns.getOrNull(2), "month", lineNumber)
            val carbonValue = parseFloatField(columns.getOrNull(3), "carbon_value", lineNumber) ?: 0f
            val documentType = parseStringField(columns.getOrNull(4), "document_type", lineNumber) ?: "data_emisi"
            val documentName = parseStringField(columns.getOrNull(5), "document_name", lineNumber) ?: "$fileName-line$lineNumber"
            val analysis = parseStringField(columns.getOrNull(6), "analysis", lineNumber)
            
            // Validate parsed values
            if (carbonValue <= 0) {
                println("UploadRepository: ✗ Line $lineNumber has invalid carbon_value: $carbonValue (must be > 0)")
                return null
            }
            
            if (year < 2000 || year > 2100) {
                println("UploadRepository: ✗ Line $lineNumber has invalid year: $year (must be 2000-2100)")
                return null
            }
            
            if (month != null && (month < 1 || month > 12)) {
                println("UploadRepository: ✗ Line $lineNumber has invalid month: $month (must be 1-12 or null)")
                return null
            }
            
            println("UploadRepository: ✓ Line $lineNumber validation passed -> company_id: $companyId, year: $year, month: $month, carbon_value: $carbonValue")
            
            // Create offline entity with proper flags
            return createOfflineEntity(
                companyId = companyId,
                year = year,
                month = month,
                carbonValue = carbonValue,
                documentType = documentType,
                documentName = documentName,
                documentPath = fileName, // Store original filename
                analysis = analysis
            )
            
        } catch (e: Exception) {
            println("UploadRepository: ✗ Exception parsing line $lineNumber: ${e.message}")
            e.printStackTrace()
            return null
        }
    }
    
    /**
     * Parses quoted CSV line (handles values with commas inside quotes)
     */
    private fun parseQuotedCsv(line: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var inQuotes = false
        var i = 0
        
        while (i < line.length) {
            val char = line[i]
            when {
                char == '"' && !inQuotes -> {
                    inQuotes = true
                }
                char == '"' && inQuotes -> {
                    if (i + 1 < line.length && line[i + 1] == '"') {
                        // Escaped quote
                        current.append('"')
                        i++ // Skip next quote
                    } else {
                        inQuotes = false
                    }
                }
                char == ',' && !inQuotes -> {
                    result.add(current.toString().trim())
                    current.clear()
                }
                else -> {
                    current.append(char)
                }
            }
            i++
        }
        
        // Add the last field
        result.add(current.toString().trim())
        return result
    }
    
    /**
     * Helper function to parse integer fields with debugging
     */
    private fun parseIntField(value: String?, fieldName: String, lineNumber: Int): Int? {
        return try {
            val parsed = value?.trim()?.toIntOrNull()
            println("UploadRepository: Line $lineNumber -> $fieldName: '$value' -> $parsed")
            parsed
        } catch (e: Exception) {
            println("UploadRepository: Line $lineNumber -> Error parsing $fieldName '$value': ${e.message}")
            null
        }
    }
    
    /**
     * Helper function to parse float fields with debugging
     */
    private fun parseFloatField(value: String?, fieldName: String, lineNumber: Int): Float? {
        return try {
            val parsed = value?.trim()?.toFloatOrNull()
            println("UploadRepository: Line $lineNumber -> $fieldName: '$value' -> $parsed")
            parsed
        } catch (e: Exception) {
            println("UploadRepository: Line $lineNumber -> Error parsing $fieldName '$value': ${e.message}")
            null
        }
    }
    
    /**
     * Helper function to parse string fields with debugging
     */
    private fun parseStringField(value: String?, fieldName: String, lineNumber: Int): String? {
        val parsed = value?.trim()?.takeIf { it.isNotBlank() }
        println("UploadRepository: Line $lineNumber -> $fieldName: '$value' -> '$parsed'")
        return parsed
    }

    /**
     * Converts URI to File for upload
     */
    private suspend fun createFileFromUri(uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                inputStream?.let { stream ->
                    val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.csv")
                    val outputStream = FileOutputStream(tempFile)
                    
                    stream.copyTo(outputStream)
                    stream.close()
                    outputStream.close()
                    
                    tempFile
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
} 