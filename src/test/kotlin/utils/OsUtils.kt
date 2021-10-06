package utils

import io.appium.java_client.AppiumDriver
import tests.constants.TestResults
import utils.constants.TextColour

object OsUtils {

    private var OS: String? = null
    private var isOS: Boolean? = null
    private var isAndroid: Boolean? = null

    @JvmStatic
    val osName: String?
        get() {
            if (OS == null) {
                OS = System.getProperty("os.name").lowercase()
            }
            return OS
        }

    @JvmStatic
    val isMAC: Boolean
        get() = osName!!.contains("mac")

    @JvmStatic
    val isLinux: Boolean
        get() = osName!!.contains("linux")

    @JvmStatic
    val isWindows: Boolean
        get() = osName!!.contains("windows")


    @JvmStatic
    fun isIOS(driver: AppiumDriver<*>?): Boolean {
        if (isOS == null) {
            val platformName =
                (driver as AppiumDriver<*>).capabilities.getCapability("platformName").toString().uppercase()
            isOS = platformName == "MAC" || platformName == "IOS"
        }
        return isOS!!
    }

    @JvmStatic
    fun isAndroid(driver: AppiumDriver<*>?): Boolean {
        if (isAndroid == null) {
            val platformName =
                (driver as AppiumDriver<*>).capabilities.getCapability("platformName").toString().uppercase()
            isAndroid = platformName == "LINUX" || platformName == "ANDROID"
        }
        return isAndroid!!
    }

    @JvmStatic
    fun returnTestResultDescription(testResult: Int): String = when (testResult) {
        1 -> "${TextColour.GREEN.colour}${TestResults.PASSED.uppercase()}${TextColour.DEFAULT.colour}"
        2 -> "${TextColour.RED.colour}${TestResults.FAILED.uppercase()}${TextColour.DEFAULT.colour}"
        3 -> "${TextColour.BLUE.colour}${TestResults.SKIPPED.uppercase()}${TextColour.DEFAULT.colour}"
        else -> "<<<Unknown test result>>>"
    }
}