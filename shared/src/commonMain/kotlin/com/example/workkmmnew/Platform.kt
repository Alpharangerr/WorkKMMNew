package com.example.workkmmnew

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform