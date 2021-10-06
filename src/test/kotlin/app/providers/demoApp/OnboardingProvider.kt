package app.providers.demoApp

import io.appium.java_client.AppiumDriver
import org.testng.asserts.SoftAssert
import screens.demoApp.login.LogInScreen

class OnboardingProvider(
    private val driver: AppiumDriver<*>?,
    private val softAssert: SoftAssert
) {

    fun logInScreen(): LogInScreen {
        return LogInScreen(driver, softAssert)
    }

}