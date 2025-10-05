package com.example.find_my_ride

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "find_my_ride",
    ) {
        App()
    }
}