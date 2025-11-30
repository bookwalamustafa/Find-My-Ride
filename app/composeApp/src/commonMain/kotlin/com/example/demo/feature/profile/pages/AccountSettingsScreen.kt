package com.example.demo.feature.profile.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demo.feature.profile.AccountSettingsState
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(
    state: AccountSettingsState,
    onChange: (AccountSettingsState) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                .background(Color(0xFFF5F5F7))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DrexelBlue,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = DrexelBlue,
                cursorColor = DrexelBlue
            )

            Text("Profile", style = MaterialTheme.typography.titleMedium, color = DrexelBlue)

            OutlinedTextField(
                value = state.fullName,
                onValueChange = { onChange(state.copy(fullName = it)) },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { onChange(state.copy(email = it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            OutlinedTextField(
                value = state.phone,
                onValueChange = { onChange(state.copy(phone = it)) },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(Modifier.height(8.dp))

            Text("Security", style = MaterialTheme.typography.titleMedium, color = DrexelBlue)

            OutlinedTextField(
                value = state.password,
                onValueChange = { onChange(state.copy(password = it)) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DrexelGold,
                    contentColor = DrexelBlue
                )
            ) { Text("Update Account") }

            Spacer(Modifier.height(24.dp))

            Text("Danger Zone", style = MaterialTheme.typography.titleMedium, color = Color.Red)

            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.Red),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text("Delete Account")
            }
        }
    }
}
