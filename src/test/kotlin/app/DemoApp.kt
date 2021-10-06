package app

import app.providers.demoApp.HomeProvider
import app.providers.demoApp.OnboardingProvider
import io.appium.java_client.AppiumDriver
import org.testng.asserts.SoftAssert

class DemoApp(
    private val driver: AppiumDriver<*>?,
    private val softAssert: SoftAssert
) {

    fun onboarding(): OnboardingProvider {
        return OnboardingProvider(driver, softAssert)
    }

    fun home(): HomeProvider {
        return HomeProvider(driver,softAssert)
    }

}