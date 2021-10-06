package screens

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.PerformsTouchActions
import io.appium.java_client.TouchAction
import io.appium.java_client.pagefactory.AppiumFieldDecorator
import io.appium.java_client.touch.TapOptions
import io.appium.java_client.touch.offset.ElementOption
import org.openqa.selenium.*
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.asserts.SoftAssert
import screens.constants.Timer
import utils.Logger.log
import utils.Logger.logError
import utils.Logger.setTag
import utils.OsUtils.isAndroid
import utils.OsUtils.isIOS
import utils.Sleep.sleep
import java.time.Duration

open class PageActions protected constructor(protected var driver: WebDriver, protected var softAssert: SoftAssert) {
    open val TAG = setTag("PageActions()")

    val isIOS: Boolean
        get() = isIOS(driver as AppiumDriver<*>)

    val isAndroid: Boolean
        get() = isAndroid(driver as AppiumDriver<*>)

    @Synchronized
    protected fun setLookTiming(sec: Int) {
        if (sec == -1) setDefaultTiming() else PageFactory.initElements(
            AppiumFieldDecorator(
                driver,
                Duration.ofSeconds(sec.toLong())
            ), this
        )
    }

    private fun setDefaultTiming() {
        PageFactory.initElements(
            AppiumFieldDecorator(driver, Duration.ofSeconds(Timer.DEFAULT_LOOK_TIME.toLong())),
            this
        )
    }

    protected fun sendKeys(elements: List<MobileElement>?, txt: String?, num: Int = 0): Boolean {
        val startTime = System.currentTimeMillis()
        do {
            try {
                (elements?.get(num) as MobileElement).sendKeys(txt)
                sleep(Timer.TAP_DELAY)
                return true
            } catch (e: ElementNotInteractableException) {
                log("${TAG}sendKeys(): retry...")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            sleep(Timer.TAP_DELAY)
        } while (System.currentTimeMillis() < startTime + Timer.SEND_KEYS_DELAY)
        return false
    }

    protected fun isLoaded(mainContainer: List<MobileElement?>, sec: Int = -1): Boolean {
        setLookTiming(sec)
        val isLoaded: Boolean = mainContainer.isNotEmpty()
        setDefaultTiming()
        return isLoaded
    }

    // Native tap

    protected fun tap(elements: List<MobileElement?>?, num: Int = 0): Boolean {
        try {
            return tap(elements!![num])
        } catch (e: Exception) {
            // ignore
        }
        return false
    }

    /**
     * @param element    - element to tap
     * @param times - how many times to tap
     * @return
     */
    private fun tap(element: MobileElement?, times: Int = 1): Boolean {
        var tapOptions: TapOptions?
        var result = false
        if (element == null) return false
        val wait = WebDriverWait(driver, Timer.WEBDRIVER_WAIT) // 2 sec
        for (i in 0 until times) {
            for (j in 0..1) { // 2 tries to make tap. 2 x 2 = 4 sec MAX! after element found.
                // to avoid StaleElementReferenceException on Android
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(element))
                } catch (e: Exception) {
                    //
                }
                try {
                    tapOptions = TapOptions().withElement(ElementOption.element(element))
                    PlatformTouchAction(driver as AppiumDriver<*>).tap(tapOptions).perform()
                    result = true
                    break
                } catch (e: StaleElementReferenceException) {
                    log(TAG + "tap(): try " + j + " : FAILED with StaleElementReferenceException", true)
                    sleep(1500)
                } catch (e: Exception) {
                    if (e.message!!.contains("Could not proxy command to remote server")
                        || e.message!!.contains("is not visible on the screen")
                    ) { // try it again
                        sleep(200)
                        log(TAG + "tap(): try " + j + " : FAILED", true)
                    } else {
                        log(
                            "${TAG}tap(): try $j: Element $element ${e.message}".trimIndent(), true
                        )
                        break
                    }
                }
            }
            if (!result) break
            if (times > 1) // wait between clicks
                sleep(100)
        }
        sleep(Timer.TAP_DELAY)
        return result
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T> getText(elements: T?, num: Int = 0): String {
        return try {
            (elements as List<WebElement>)[num].text
        } catch (e1: Exception) {
            try {
                (elements as List<MobileElement>)[num].text
            } catch (e2: Exception) {
                logError(TAG + "getText(): FAILED")
                "FAILED_TO_GET_TEXT"
            }
        }
    }

    protected fun waitElementLoaded(mobileBy: By?, seconds: Int = Timer.DEFAULT_LOOK_TIME): List<MobileElement?> {
        var sec = seconds
        log(TAG + "waitElementLoaded():")
        var elements: List<MobileElement?>
        if (sec == -1) sec = Timer.DEFAULT_LOOK_TIME
        val startTime = System.currentTimeMillis()
        do {
            elements = driver.findElements(mobileBy)
            if (elements.isNotEmpty()) break
        } while (System.currentTimeMillis() < startTime + sec * 1000)
        setDefaultTiming()
        return elements
    }

    init {
        setDefaultTiming()
    }
}