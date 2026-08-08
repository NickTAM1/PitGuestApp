package org.resortguestapp.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform