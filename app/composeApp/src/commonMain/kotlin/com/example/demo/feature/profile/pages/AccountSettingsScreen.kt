package com.example.demo.feature.profile.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    // 🔹 Local state so fields are editable
    var fullName by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("john.doe@email.com") }
    var phone by remember { mutableStateOf("+1 (555) 123-4567") }
    var password by remember { mutableStateOf("********") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DrexelBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F7)) // light grey background
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Profile",
                style = MaterialTheme.typography.titleMedium,
                color = DrexelBlue
            )

            // 🔹 TextField color overrides so we don’t get purple
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DrexelBlue,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = DrexelBlue,
                focusedLabelColor = DrexelBlue
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Security",
                style = MaterialTheme.typography.titleMedium,
                color = DrexelBlue
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(Modifier.height(16.dp))

            // 🔹 Primary action button in DrexelGold
            Button(
                onClick = {
                    // TODO: hook up to real update logic
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DrexelGold,
                    contentColor = DrexelBlue
                )
            ) {
                Text("Update Account")
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Danger Zone",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red
            )

            OutlinedButton(
                onClick = {
                    // TODO: hook up delete logic (and/or confirmation dialog)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Red
                ),
                border = BorderStroke(width = 1.dp, color = Color.Red)
            ) {
                Text("Delete Account")
            }
        }
    }
}
