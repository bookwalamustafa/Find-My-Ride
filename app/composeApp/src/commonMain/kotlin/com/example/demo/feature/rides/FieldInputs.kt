package com.example.demo.feature.rides

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.HintGrey

@Composable
fun RideInput(
    label: String,
    icon: ImageVector,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    var textState by remember { mutableStateOf("") }
    val fieldColor = Color(0xFFF3F4F6)

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = DrexelBlue,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier.fillMaxWidth(),

            placeholder = {
                Text(
                    text = placeholder,
                    color = HintGrey
                )
            },

            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = HintGrey,
                    modifier = Modifier.size(28.dp)
                )
            },

            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = fieldColor,
                unfocusedContainerColor = fieldColor,
                disabledContainerColor = fieldColor,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = DrexelBlue,
                unfocusedTextColor = DrexelBlue
            ),
            singleLine = true,
        )
    }
}