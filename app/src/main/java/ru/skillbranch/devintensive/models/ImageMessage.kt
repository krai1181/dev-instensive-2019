package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.humanizeDiff
import java.util.*

class ImageMessage(
    id: String,
    from: User?,
    chat: Chat,
    isIncoming: Boolean = false,
    date: Date = Date(),
    var image: String?
) : BaseMessage(id, from, chat, isIncoming, date) {

    override fun formatMassage(): String =
        "${from?.firstName} ${if (isIncoming) "получил(а)" else "отправил(а)"} изображение \"$image\" ${date.humanizeDiff()}"
}
