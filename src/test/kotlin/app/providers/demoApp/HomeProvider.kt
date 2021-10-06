package app.providers.demoApp

import io.appium.java_client.AppiumDriver
import org.testng.asserts.SoftAssert
import screens.demoApp.home.HomeScreen

class HomeProvider(
    private val driver: AppiumDriver<*>?,
    private val softAssert: SoftAssert
) {

    fun homeScreen(): HomeScreen {
        return HomeScreen(driver, softAssert)
    }
}