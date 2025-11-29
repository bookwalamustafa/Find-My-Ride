package com.example.rideshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.demo.AndroidRideRepository
import com.example.demo.App
import com.example.demo.FindMyRideDbProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val context = LocalContext.current
                    val repo = remember {
                        AndroidRideRepository(FindMyRideDbProvider(context))
                    }
                    App(rideRepository = repo)
                }
            }
        }
    }
}
