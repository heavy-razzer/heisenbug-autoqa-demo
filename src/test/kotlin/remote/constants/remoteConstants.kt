package remote.constants

enum class OutsourcedURLs(val url: String) {
    SLACK_POST_MESSAGE("https://slack.com/api/chat.postMessage")
}

enum class SlackChannels(val value: String, val description: String) {
    TEST("xxxxx", "autoqa-local-build"),
    DEMO_APP("xxxxx", "ci-builds")
}
