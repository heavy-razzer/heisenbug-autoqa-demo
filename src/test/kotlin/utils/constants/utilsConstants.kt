package utils.constants

enum class DatePattern(val pattern: String) {
    /*
    Date Pattern examples
    https://help.gooddata.com/cloudconnect/manual/date-and-time-format.html
    Another useful link
    https://gist.github.com/y-gagar1n/8469484
     */
    LOGS_TIMESTAMP("yyyy-MM-dd'T'HH-mm-ss")
}

object Environment {
    const val PROD = "production"
    const val STAGING = "staging"
}

object Indent {
    const val BEFORE_THREAD = 15
    const val BEFORE_CLASS = 45
    const val BEFORE_FUNCTION = 30
}

object Path {
    const val APP_FOLDER_MAC = "/Documents/Wolt/"
    const val APP_FOLDER_WIN = "c:/Wolt/"
    val USER_HOME = System.getProperty("user.home")
    val USER_DIR = System.getProperty("user.dir")
}

enum class TextColour(var colour: String) {
    DEFAULT("\u001B[0m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    BLUE("\u001B[34m"),
    CYAN("\u001B[36m"),
    PURPLE("\u001B[35m"),
    YELLOW("\u001B[33m");
}