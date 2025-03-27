package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun InputField(value: String, onValueChange: (String) -> Unit, iconRes: Int, label: String, keyboardType: KeyboardType = KeyboardType.Text) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "$label Icon",
                modifier = Modifier.size(24.dp)
            )
        },
        placeholder = { Text(label) },
        modifier = Modifier.fillMaxWidth().background(Color.White),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(0xFF27361F),
            unfocusedIndicatorColor = Color.Gray,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}