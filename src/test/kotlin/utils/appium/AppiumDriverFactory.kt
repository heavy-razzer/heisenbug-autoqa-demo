package utils.appium

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.*
import org.openqa.selenium.remote.DesiredCapabilities
import utils.Logger.sysLog
import utils.constants.TextColour
import java.net.MalformedURLException
import java.net.URL

class AppiumDriverFactory {

    fun startLocalDriver(deviceName: String): AppiumDriver<*>? {

        sysLog("startDriver(): " + TextColour.YELLOW.colour + "LOCAL" + TextColour.DEFAULT.colour)

        var driver: AppiumDriver<*>? = null
        val capabilities = DesiredCapabilities()
        val appPath = "/Users/mikhailmiroshnichenko/Documents/Demo/iOS.app"

        if (deviceName.lowercase().contains("ipad") || deviceName.lowercase().contains("iphone")) {  // iOS
            capabilities.setCapability(MobileCapabilityType.APP, appPath)
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST)
            capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.demo.test")
            capabilities.setCapability(IOSMobileCapabilityType.SIMPLE_ISVISIBLE_CHECK, true)
            capabilities.setCapability(IOSMobileCapabilityType.SEND_KEY_STRATEGY, "setValue")
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPad (8th generation)")
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS)
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "14.5")
        } else { // Android
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2)
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android")
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID)
            capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true)
            capabilities.setCapability(AndroidMobileCapabilityType.DISABLE_ANDROID_WATCHERS, true)
            capabilities.setCapability(AndroidMobileCapabilityType.NO_SIGN, "true")
            capabilities.setCapability(MobileCapabilityType.APP, appPath)
            capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, "*")
            capabilities.setCapability(AndroidMobileCapabilityType.RECREATE_CHROME_DRIVER_SESSIONS, true)
        }
        capabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true)
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 180)
        capabilities.setCapability(MobileCapabilityType.FULL_RESET, false)
        capabilities.setCapability(MobileCapabilityType.NO_RESET, false)

        val baseURL = "http://0.0.0.0:"
        val minorURL = "/wd/hub"
        val port = "4723"

        try {
            if (deviceName.lowercase().contains("ipad") || deviceName.lowercase().contains("iphone")) {
                sysLog(
                    "startDriver(): "
                            + TextColour.PURPLE.colour
                            + "Local IOSDriver"
                            + TextColour.DEFAULT.colour
                )
                driver = IOSDriver<MobileElement>(URL(baseURL + port + minorURL), capabilities)
            } else {
                sysLog(
                    "startDriver(): "
                            + TextColour.PURPLE.colour
                            + "Local AndroidDriver"
                            + TextColour.DEFAULT.colour
                )
                driver = AndroidDriver<MobileElement>(URL(baseURL + port + minorURL), capabilities)
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        sysLog("startDriver(): completed.")
        return driver
    }

    fun startBrowserStackDriver(
        testName: String,
        timeStamp: String
    ): AppiumDriver<*>? {
        var driver: AppiumDriver<*>? = null
        sysLog("startDriver(): " + TextColour.YELLOW.colour + "BrowserStack" + TextColour.DEFAULT.colour)
        val cloudServer = "hub-eu.browserstack.com/wd/hub"
        val userName = "xxx"
        val userKey = "xxx"
        val url = "https://${userName}:${userKey}@${cloudServer}"
        val deviceName = "iPhone 8"
        val platformVersion = "14"
        val capabilities = DesiredCapabilities()
        capabilities.setCapability("device", deviceName)
        capabilities.setCapability("os_version", platformVersion)
        capabilities.setCapability("project", "Demo product")
        capabilities.setCapability("build", timeStamp) // Place all tests from suite in the same place
        capabilities.setCapability("name", testName)
        capabilities.setCapability("browserstack.networkLogs", true)
        capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true)
        capabilities.setCapability("app", "appInTheCloud")
        capabilities.setCapability("browserstack.appium_version", "1.20.2")
        val isAndroid = deviceName.lowercase().contains("android")

        try {
            if (isAndroid) {
                sysLog(
                    "startDriver(): "
                            + TextColour.PURPLE.colour
                            + "BrowserStack Cloud AndroidDriver"
                            + TextColour.DEFAULT.colour
                )
                driver = AndroidDriver<MobileElement>(URL(url), capabilities)
            } else {
                sysLog(
                    "startDriver(): "
                            + TextColour.PURPLE.colour
                            + "BrowserStack Cloud IOSDriver"
                            + TextColour.DEFAULT.colour
                )
                driver = IOSDriver<MobileElement>(URL(url), capabilities)
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        sysLog("startDriver(): completed")
        return driver
    }
}
