package com.example.tumbuhnyata.data.model

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("id_perusahaan")
    val id: Int,
    
    @SerializedName("nama_perusahaan")
    val companyName: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("no_telp")
    val phoneNumber: String,
    
    @SerializedName("NIB")
    val nib: String,
    
    @SerializedName("alamat")
    val address: String
)

data class ProfileResponse(
    @SerializedName("data")
    val data: Profile
)