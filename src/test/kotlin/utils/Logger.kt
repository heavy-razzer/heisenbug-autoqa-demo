package utils

import utils.constants.Indent
import utils.constants.TextColour

object Logger {

    var message: String = ""

    @JvmStatic
    fun log(text: Any, vararg args: Any?) {
        message = String.format(text.toString(), *args)
        println(
            TextColour.GREEN.colour +
                    "[TEST]".padEnd(Indent.BEFORE_THREAD) +
                    Thread.currentThread().name.padEnd(Indent.BEFORE_CLASS) +
                    TextColour.DEFAULT.colour +
                    message
        )
    }

    @JvmStatic
    fun logError(text: Any, vararg args: Any?) {
        message = String.format(text.toString(), *args)
        println(
            TextColour.RED.colour +
                    "[ERROR]".padEnd(Indent.BEFORE_THREAD) +
                    TextColour.DEFAULT.colour +
                    message
        )
    }

    @JvmStatic
    fun sysLog(text: Any, vararg args: Any?) {
        message = String.format(text.toString(), *args)
        println(
            TextColour.BLUE.colour
                    + "[SYSTEM]".padEnd(Indent.BEFORE_THREAD)
                    + TextColour.DEFAULT.colour
                    + message
        )
    }

    fun print(text: String, colour: TextColour) {
        println(colour.colour + text.toString() + TextColour.DEFAULT.colour)
    }

    @JvmStatic
    fun setTag(tag: String): String {
        return tag.padEnd(Indent.BEFORE_FUNCTION) + "| "
    }
}