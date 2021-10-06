package screens

import io.appium.java_client.AppiumDriver
import io.qameta.allure.Step
import org.openqa.selenium.WebDriver
import org.testng.asserts.SoftAssert
import utils.Logger.log
import utils.Logger.setTag

open class ElementActions(driver: WebDriver, softAssert: SoftAssert) : PageActions(
    driver, softAssert
) {
    override val TAG = setTag("ElementActions()")

    @Step("Dismiss Alert")
    fun alertDismiss(): Boolean {
        log(TAG + "alertDismiss():")
        try {
            (driver as AppiumDriver<*>).executeScript("mobile: dismissAlert")
            return true
        } catch (e: Exception) {
        }
        return false
    }

    @Step("Accept Alert")
    fun alertAccept(): Boolean {
        log(TAG + "alertAccept():")
        try {
            (driver as AppiumDriver<*>).executeScript("mobile: acceptAlert")
            return true
        } catch (e: Exception) {
        }
        return false
    }
}