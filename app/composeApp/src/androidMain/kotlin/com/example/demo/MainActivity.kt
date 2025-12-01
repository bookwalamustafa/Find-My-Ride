package com.example.rideshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.demo.App
import com.example.demo.AndroidRideRepository
import com.example.demo.AndroidAuthRepository
import com.example.demo.FindMyRideDbProvider
import com.example.demo.feature.profile.data.AndroidProfileRepository
import com.example.demo.feature.messages.data.AndroidMessagesRepository
import com.example.demo.feature.rides.AvailableRidesScreen
import com.example.demo.ui.theme.FindRideScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val context = LocalContext.current
                    val dbProvider = remember { FindMyRideDbProvider(context) }

                    val rideRepo = remember { AndroidRideRepository(dbProvider) }
                    val profileRepo = remember { AndroidProfileRepository(dbProvider) }
                    val authRepo = remember { AndroidAuthRepository(dbProvider) }
                    val messagesRepo = remember { AndroidMessagesRepository(dbProvider) }

                    App(
                        rideRepository = rideRepo,
                        profileRepository = profileRepo,
                        authRepository = authRepo,
                        messagesRepository = messagesRepo
                    )
                }
            }
        }
    }
}
