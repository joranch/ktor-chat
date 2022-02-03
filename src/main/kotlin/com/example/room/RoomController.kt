package com.example.room

import com.example.data.MessageDataSource
import com.example.data.model.Message
import io.ktor.http.cio.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(private val messageDataSource: MessageDataSource) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(username: String, sessionId: String, socket: WebSocketSession) {
        if (members.contains(username)) {
            throw MemberAlreadyExistsExcpetion()
        }

        members[username] = Member(username, sessionId, socket)
    }

    suspend fun sendMessage(senderUsername: String, message: String) {
        members.values.forEach { member ->
            val messageEntity = Message(
                text = message,
                username = senderUsername,
                timestamp = System.currentTimeMillis()
            )
            messageDataSource.insertMessage(messageEntity)

            val parsedMessage = Json.encodeToString(messageEntity)
            member.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMessages(): List<Message> {
        return messageDataSource.getAllMessages()
    }

    suspend fun tryDisconnect(username: String) {

        if (members.containsKey(username)) {
            members[username]?.socket?.close()
            members.remove(username)
        }
    }
}