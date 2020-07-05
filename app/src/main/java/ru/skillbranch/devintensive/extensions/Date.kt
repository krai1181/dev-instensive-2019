package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.extensions.TimeUnits.Companion.plural
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR


fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time
    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diff: Long = date.time - this.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return when {
        seconds in 0..1 -> "только что"
        seconds in 1..45 -> "несколько секунд назад"
        seconds in 45..75 -> "минуту назад"
        (seconds > 75).and(minutes < 45) -> TimeUnits.MINUTE.plural(minutes.toInt()) + " назад"
        minutes in 45..75 -> "час назад"
        hours in 2..4 -> "$hours часа назад"
        hours == 21L -> "21 час назад"
        (minutes > 75).and(hours < 22) -> "$hours часов назад"
        hours in 22..26 -> "день назад"
        (hours > 26).and(days < 360) -> TimeUnits.DAY.plural(days.toInt()) +" назад"
        days > 360 -> "более года назад"
        else -> "Not valid date"
    }

}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    companion object {
        fun TimeUnits.plural(value: Int): String {
            fun getReminder(x: Int) = x % 10
            return when (this) {
                SECOND -> {
                    "$value " + when {
                        value in 11..14 -> "секунд"
                        getReminder(value) == 1 -> "секунду"
                        getReminder(value) in 2..4 -> "секунды"
                        else -> "секунд"
                    }
                }
                MINUTE -> "$value " + when {
                    value in 11..14 -> "минут"
                    getReminder(value) == 1 -> "минуту"
                    getReminder(value) in 2..4 -> "минуты"
                    else -> "минут"
                }
                HOUR -> "$value " + when {
                    value in 11..14 -> "часов"
                    getReminder(value) == 1 -> "час"
                    getReminder(value) in 2..4 -> "часа"
                    else -> "часов"
                }
                else -> "$value " + when {
                    value in 11..14 -> "дней"
                    getReminder(value) == 1 -> "день"
                    getReminder(value) in 2..4 -> "дня"
                    else -> "дней"
                }
            }
        }
    }
}