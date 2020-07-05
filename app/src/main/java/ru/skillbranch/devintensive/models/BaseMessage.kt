package ru.skillbranch.devintensive.models

import java.util.*

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {

    abstract fun formatMassage(): String

    companion object AbstractFactory {
        var lastId = -1

        fun makeMassage(
            from: User?,
            chat: Chat,
            date: Date = Date(),
            type: String = "text",
            payload: Any
        ): BaseMessage {
            lastId++
            return when (type) {
                "image" -> ImageMessage(
                    "$lastId",
                    from,
                    chat,
                    true,
                    date = date,
                    image = payload as String
                )
                else -> TextMessage("$lastId", from, chat, date = date, text = payload as String)

            }
        }

    }
}