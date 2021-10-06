package screens.demoApp.login

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.pagefactory.AndroidFindBy
import io.appium.java_client.pagefactory.iOSXCUITFindBy
import io.qameta.allure.Step
import org.testng.Assert
import org.testng.asserts.SoftAssert
import screens.ElementActions
import screens.demoApp.home.HomeScreen
import utils.Logger.log
import utils.Logger.setTag

class LogInScreen(
    driver: AppiumDriver<*>?,
    softAssert: SoftAssert?
) : ElementActions(
    driver!!, softAssert!!
) {
    override val TAG = setTag("LogInScreen()")

    // =========
    // LOCATORS

    @iOSXCUITFindBy(id = "loginView.root")
    @AndroidFindBy(id = "loginView.root")
    private val mainContainer: List<MobileElement>? = null

    @iOSXCUITFindBy(id = "loginView.usernameField")
    @AndroidFindBy(id = "loginView.usernameField")
    private val userNameInput: List<MobileElement>? = null

    @iOSXCUITFindBy(id = "loginView.passwordField")
    @AndroidFindBy(id = "loginView.passwordField")
    private val passwordInput: List<MobileElement>? = null

    @iOSXCUITFindBy(id = "loginView.signInButton")
    @AndroidFindBy(id = "loginView.signInButton")
    private val signInButton: List<MobileElement?>? = null

    // =========
    // STEPS

    @get:Step("Check 'Log In' screen loaded")
    val isLoginScreenLoaded: LogInScreen
        get() {
            log(TAG + "isLoginScreenLoaded(): ")
            Assert.assertTrue(isLoaded(mainContainer!!), "${TAG}isLoginScreenLoaded: 'LogIn' screen NOT loaded")
            return this
        }

    @Step("Set user name: '{userName}'")
    fun setUserName(userName: String): LogInScreen = also {
        log(TAG + "setUserName(): " + userName)
        Assert.assertTrue(sendKeys(userNameInput, userName), "${TAG}setUserName(): FAILED")
    }

    @Step("Set password")
    fun setPassword(password: String): LogInScreen = also {
        log(TAG + "setPassword(): ")
        Assert.assertTrue(sendKeys(passwordInput, password), "${TAG}setPassword(): FAILED")
    }

    @Step("Tap 'Sign Up' button")
    fun tapSignInButton(): HomeScreen {
        log(TAG + "tapSignInButton():")
        Assert.assertTrue(tap(signInButton), "${TAG}tapSignInButton(): FAILED")
        return HomeScreen(driver as AppiumDriver<*>, softAssert)
    }
}