package utils

import utils.Logger.logError

object Sleep {

    @JvmStatic
    fun sleep(msec: Int) {
        try {
            Thread.sleep(msec.toLong())
        } catch (e: Exception) {
            logError("Can't sleep here.")
        }
    }
}