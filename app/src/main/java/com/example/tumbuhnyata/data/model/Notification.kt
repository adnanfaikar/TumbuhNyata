package com.example.tumbuhnyata.data.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.*
import java.lang.reflect.Type

// Custom deserializer untuk menangani boolean atau integer
class BooleanIntDeserializer : JsonDeserializer<Int> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Int {
        return when {
            json.isJsonPrimitive -> {
                val primitive = json.asJsonPrimitive
                when {
                    primitive.isBoolean -> if (primitive.asBoolean) 1 else 0
                    primitive.isNumber -> primitive.asInt
                    else -> 0
                }
            }
            else -> 0
        }
    }
}

data class Notification(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("user_id")
    val userId: String,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("is_read")
    @JsonAdapter(BooleanIntDeserializer::class)
    val isRead: Int,
    
    @SerializedName("created_at")
    val createdAt: String
) {
    fun isReadBool(): Boolean = isRead > 0
} 