package tests

import app.DemoApp
import io.appium.java_client.AppiumDriver
import org.json.simple.JSONObject
import org.testng.Assert
import org.testng.IResultMap
import org.testng.ITestContext
import org.testng.ITestResult
import org.testng.annotations.*
import org.testng.asserts.SoftAssert
import remote.constants.SlackChannels
import remote.slack.SlackHelper
import remote.slack.setTextBlock
import tests.constants.TestResults
import utils.DateProcessor
import utils.Logger.print
import utils.Logger.sysLog
import utils.OsUtils
import utils.appium.AppiumDriverFactory
import utils.constants.DatePattern
import utils.constants.TextColour
import java.lang.reflect.Method
import java.time.LocalDateTime


open class BaseTest {

    var driver: AppiumDriver<*>? = null

    lateinit var demoApp: DemoApp
    lateinit var softAssert: SoftAssert

    private val timeStamp = DateProcessor.getLocalDateTimeFormat(LocalDateTime.now(), DatePattern.LOGS_TIMESTAMP)
    private var slackMessagePayload = arrayListOf<JSONObject>()

    @Parameters("deviceName", "runner")
    @BeforeSuite(alwaysRun = true)
    fun beforeSuite(
        @Optional("") deviceName: String,
        @Optional("") runner: String
    ) {
        println("")
        print("  BeforeSuite", TextColour.GREEN)

        print("TimeStamp: $timeStamp", TextColour.BLUE)
    }

    @AfterSuite(alwaysRun = true)
    fun tearDown(iTestContext: ITestContext) {
        println("")
        print("  AfterSuite", TextColour.GREEN)
        print(" ==================", TextColour.YELLOW)
        print("  Passed tests:  " + iTestContext.passedTests.size(), TextColour.GREEN)
        print("  Skipped tests: " + iTestContext.skippedTests.size(), TextColour.BLUE)
        print("  Failed tests:  " + iTestContext.failedTests.size(), TextColour.RED)
        print(" ==================\n", TextColour.YELLOW)
        print("\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n", TextColour.YELLOW)
    }

    @Parameters("deviceName", "runner")
    @BeforeMethod(alwaysRun = true)
    fun beforeMethod(
        @Optional("") deviceName: String,
        @Optional("") runner: String,
        method: Method
    ) {
        println("")
        print("  BeforeMethod", TextColour.GREEN)
        Thread.currentThread().name = method.name
        softAssert = SoftAssert()

        when (runner) {
            "local" -> {
                driver = AppiumDriverFactory().startLocalDriver(deviceName)
            }
            "browserstack" -> {
                driver = AppiumDriverFactory().startBrowserStackDriver(method.name, timeStamp)
            }
            else -> {
                Assert.fail("Unsupported platform name: '${runner}'. Can be: local, browserstack.")
            }
        }

        demoApp = DemoApp(driver, softAssert)
    }

    @Parameters("runner")
    @AfterMethod(alwaysRun = true)
    fun afterMethod(
        result: ITestResult,
        @Optional("") runner: String
    ) {
        println("")
        print("  AfterMethod", TextColour.GREEN)
        val testResultStatus = result.status
        println("Test result: " + OsUtils.returnTestResultDescription(testResultStatus))

        // Terminate driver / disconnect from cloud device
        sysLog("Quit driver")
        try {
            if (driver != null) driver!!.quit()
        } catch (e: Exception) {
            sysLog("Unable to terminate driver")
        }

        print("\n ----------------------------\n", TextColour.YELLOW)
    }

    @Parameters("product", "deviceName", "runner", "env")
    @AfterTest(alwaysRun = true)
    fun afterTest(
        iTestContext: ITestContext,
        @Optional("") product: String,
        @Optional("") deviceName: String,
        @Optional("") runner: String,
        @Optional("") env: String
    ) {
        println("")
        print("  AfterTest:", TextColour.GREEN)
        postTestResultsToSlack(product, deviceName, runner, env, iTestContext)
    }

    private fun createTestResultsBlock(results: ITestContext, testResultString: String) {

        // Process tests from one group only
        val resultMap: IResultMap
        var icon = ""

        when (testResultString) {
            TestResults.PASSED -> {
                resultMap = results.passedTests
                icon = "✅"
            }
            TestResults.FAILED -> {
                resultMap = results.failedTests
                icon = "❌"
            }
            else -> resultMap = results.skippedTests
        }

        // Print section caption
        var message = "$icon $testResultString tests:"

        // Process individual test
        var lastTestClass = ""
        if (resultMap.size() > 0) {
            for (executedTest in resultMap.allResults) {
                val currentClassName = executedTest.testClass.name
                if (!currentClassName.equals(lastTestClass)) { // If this test is from new class then print class name
                    message += "\n\t*$currentClassName*"
                    lastTestClass = currentClassName
                }
                message += "\n\t\t${executedTest.name}"
            }

            slackMessagePayload.add(setTextBlock(message))
        }
    }

    private fun postTestResultsToSlack(
        product: String,
        deviceName: String,
        runner: String,
        environment: String,
        results: ITestContext
    ) {

        // Send notification for CI builds only
        if (runner != "local") {

            // Print Passed/Total test count
            val totalTestsCount: Int =
                results.passedTests.size() + results.failedTests.size() + results.skippedTests.size()
            val passedTestsCount: Int = results.passedTests.size()
            // Add @here notification if all tests failed and suite has more than 1 test
            // So no notification if suite with 1 test failed
            val notifyHere: String = if ((passedTestsCount == 0) && (totalTestsCount > 1)) "@here" else ""
            slackMessagePayload.add(setTextBlock(":information_source: Tests passed: $passedTestsCount / $totalTestsCount $notifyHere"))

            // Print sections with Failed tests
            createTestResultsBlock(results, TestResults.FAILED)

            // Get which channel to send the message
            val channel: SlackChannels =
                when (product) {
                    "demoApp" -> {
                        SlackChannels.DEMO_APP
                    }
                    else -> { // Temporary channel for development and debugging
                        SlackChannels.TEST
                    }
                }

            /*
            Post message.
            Do not fail run if message wasn't sent but post error message
             */
            try {
                SlackHelper().sendMessage(channel, slackMessagePayload.toString())
            } catch (e: AssertionError) {
                sysLog("${e.message}")
            }
        }
    }
}
