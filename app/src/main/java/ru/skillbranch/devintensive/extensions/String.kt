package ru.skillbranch.devintensive.extensions

import java.lang.StringBuilder


fun String.truncate(charsAmount: Int = 16): String =
    "${this.removeRange(charsAmount, this.length).trim()}..."

fun String.stripHtml(): String {
    val sb = StringBuilder(this)
    var open: Int
    while (sb.indexOf("<").also{ open = it } != -1) {
        val close: Int = sb.indexOf(">", open + 1)
        sb.delete(open, close + 1)
    }
    return sb.toString().replace("\\s+".toRegex()) { it.value[0].toString() }
}


