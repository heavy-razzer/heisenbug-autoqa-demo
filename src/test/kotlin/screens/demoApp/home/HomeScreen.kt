package screens.demoApp.home

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.pagefactory.AndroidFindBy
import io.appium.java_client.pagefactory.iOSXCUITFindBy
import io.qameta.allure.Step
import org.testng.Assert
import org.testng.asserts.SoftAssert
import screens.ElementActions
import screens.demoApp.login.LogInScreen
import utils.Logger
import utils.Logger.log

class HomeScreen(
    driver: AppiumDriver<*>?,
    softAssert: SoftAssert?
) : ElementActions(
    driver!!, softAssert!!
) {
    override val TAG = Logger.setTag("HomeScreen()")

    // =========
    // LOCATORS

    @iOSXCUITFindBy(id = "Home.root")
    @AndroidFindBy(id = "Home.root")
    private val mainContainer: List<MobileElement>? = null

    @iOSXCUITFindBy(id = "Home.title")
    @AndroidFindBy(id = "Home.title")
    private val titleText: List<MobileElement>? = null

    // =========
    // STEPS

    @get:Step("Check 'Home' screen loaded")
    val isHomeScreenLoaded: HomeScreen
        get() {
            log(TAG + "isHomeScreenLoaded():")
            Assert.assertTrue(isLoaded(mainContainer!!), "${TAG}isHomeScreenLoaded: 'Home' screen NOT loaded")
            return this
        }

    @Step("Get title text")
    fun getTitleText(): String {
        log(TAG+"getTitleText():")
        return getText(titleText)
    }
}