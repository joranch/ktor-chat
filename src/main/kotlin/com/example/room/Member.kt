package com.example.room

import io.ktor.http.cio.websocket.*

data class Member(
    val username: String,
    val sessionsId: String,
    val socket: WebSocketSession
)