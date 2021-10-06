package remote.slack

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.simple.JSONObject
import org.testng.Assert
import remote.constants.OutsourcedURLs
import remote.constants.SlackChannels
import utils.Logger
import utils.Logger.sysLog

/*
Useful links about setting up Slack notifications:
https://api.slack.com/apps/
https://api.slack.com/authentication/basics
https://api.slack.com/messaging/sending

Action points:
- Create Slack app
- Add permission "chat:write" to send messages
- Add app to Slack workspace (must be authorised by devops)
- Crate new channel
- /invite app to this channel
- Get channel iD from it's properties
- Send message to channel by ID
 */

class SlackHelper {
    private val TAG = Logger.setTag("ApiHelper()")

    private val token = "xxx"

    private val client = OkHttpClient()

    fun sendMessage(channel: SlackChannels, message: String, isMarkDown: Boolean = true) {
        sysLog("Sending message to Slack '${channel.description}' channel")

        val payload = JSONObject()
        payload["channel"] = channel.value
        if (isMarkDown) {
            payload["text"] = "autoqa notification"
            payload["blocks"] = message
        } else {
            payload["text"] = message
        }

        val request = Request.Builder()
            .url(OutsourcedURLs.SLACK_POST_MESSAGE.url)
            .header("Authorization", "Bearer $token")
            .addHeader("content-type", "application/json")
            .post(payload.toString().toRequestBody())
            .build()

        client.newCall(request).execute().use { response ->
            val responseJson: JsonNode = ObjectMapper().readTree(response.body?.string()?.trim())
            Assert.assertTrue(
                responseJson.get("ok").asBoolean(),
                "${TAG}sendMessage(): Failed sending message to channel '${channel.name}'"
            )
        }
    }
}
/*

Composing complex messages for Slack:
https://app.slack.com/block-kit-builder
https://api.slack.com/methods/chat.postMessage
 */

fun setTextBlock(payload: String): JSONObject {
    val result = JSONObject()
    result["type"] = "section"
    result["text"] = createMarkdownElement(payload)
    return result
}

fun setHeader(value: String): JSONObject {
    val result = JSONObject()
    result["type"] = "header"
    result["text"] = createPlainTextElementElement(value)
    return result
}

private fun createMarkdownElement(string: String): JSONObject {
    val result = JSONObject()
    result["type"] = "mrkdwn"
    result["text"] = string
    return result
}

private fun createPlainTextElementElement(string: String): JSONObject {
    val result = JSONObject()
    result["type"] = "plain_text"
    result["text"] = string
    result["emoji"] = true
    return result
}
