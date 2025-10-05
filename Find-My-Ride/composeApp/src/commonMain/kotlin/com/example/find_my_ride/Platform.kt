package com.example.find_my_ride

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform