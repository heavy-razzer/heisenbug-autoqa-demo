package utils

import utils.constants.DatePattern
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateProcessor {

    @JvmStatic
    fun getLocalDateTimeFormat(date: LocalDateTime, pattern: DatePattern): String {
        val formatter = DateTimeFormatter.ofPattern(pattern.pattern)
        return date.format(formatter)
    }
}