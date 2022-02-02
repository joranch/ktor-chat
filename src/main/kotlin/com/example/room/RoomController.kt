package com.example.room

import com.example.data.MessageDataSource
import io.ktor.http.cio.websocket.*
import java.util.concurrent.ConcurrentHashMap

class RoomController(private val messageDataSource: MessageDataSource) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(username: String, sessionId: String, socket: WebSocketSession) {
        if(members.contains(username)) {
            throw MemberAlreadyExistsExcpetion()
        }

        members[username] = Member(username, sessionId, socket)
    }
}