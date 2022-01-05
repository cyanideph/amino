package ru.aminocoins.amino.wapi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import ru.aminocoins.amino.api.Actions
import ru.aminocoins.amino.api.Api
import ru.aminocoins.amino.api.Words
import ru.aminocoins.amino.api.goodNarviiResponseOrThrow
import ru.aminocoins.amino.api.plugin.Plugin
import ru.aminocoins.amino.signer.Signer
import ru.aminocoins.amino.wapi.`object`.post.Post
import ru.aminocoins.amino.wapi.`object`.user.User

fun main() {
    val wApi = WApi.build {
        threadsCount *= 2
    }.apply {
        install(Signer) {
            baseUrl = "https://myreapidomain.com/"
            apiPath = "my/reapi/path/"
            apiKeyHeaderName = "myApiKeyHeader"
            apiKey = "myApiKey"
            signaturePath = "my/signature/path"
            requestConvertStrategy = {
                buildJsonObject {
                    put("dataForSignature", it)
                }
            }
            responseConvertStrategy = {
                it["signature"]!!.jsonPrimitive.content
            }
        }
    }
    runBlocking(Dispatchers.IO) {
        val user = wApi.authByEmail("email", "secret")
        val community = user.resolveCommunity("2")
        val post = community.resolvePost("postId", Post.Type.Blog)
        for (i in 1..100) {
            async {
                post.pay(500)
            }.start()
        }
    }
}

@Suppress("MemberVisibilityCanBePrivate")
class WApi(
    private val api: Api,
) {
    @Suppress("RedundantSuspendModifier", "unused")
    suspend fun authBySerializedData(data: String): User {
        val jsonObject: JsonObject = Json.decodeFromString(data)
        val apiKey: User.ApiKey = Json.decodeFromJsonElement(jsonObject["apiKey"]!!)
        return User(
            wApi = this, api = api, id = jsonObject["id"]!!.jsonPrimitive.content,
            login = jsonObject["login"]!!.jsonPrimitive.content, apiKey = apiKey
        )
    }

    @Suppress("unused")
    private fun refreshUserIfNeeds(user: User): User = if (user.apiKey.valid) user else refreshUser(user)

    @Suppress("UNUSED_PARAMETER")
    private fun refreshUser(user: User): User {
        TODO("Requires specific information")
    }

    suspend fun authByEmail(email: String, secret: String): User {
        val body = buildJsonObject {
            put("secret", "0 $secret")
            put("email", email)
            put("clientType", 100)
            put("systemPushEnabled", 0)
            put("locale", "en_UA")
            put("timestamp", System.currentTimeMillis())
            put("action", "normal")
            put("bundleID", "com.narvii.master")
            put("timezone", 120)
            put("v", 2)
            put("clientCallbackURL", "narviiapp://default")
            put("deviceID", api.header(Words.HEADER_DEVICE_ID))
        }
        val response = runBlocking {
            api.postGlobalAction(Actions.LOGIN, body)
        }.goodNarviiResponseOrThrow
        return User(
            this, api, response["auid"]!!.jsonPrimitive.content, email, User.ApiKey(
                response["sid"]!!.jsonPrimitive.content,
                (System.currentTimeMillis() / 1000) + 86400,
                response["secret"]!!.jsonPrimitive.content
            )
        )
    }

    fun <TPlugin : Any, TConfiguration : Any> install(
        plugin: Plugin<TPlugin, TConfiguration>,
        configure: TConfiguration.() -> Unit,
    ) {
        plugin.install(api, configure)
    }

    interface Builder {

        var threadsCount: Int

        fun build(): WApi
    }

    companion object {

        val builder: Builder
            get() = GracefulWApiBuilder()

        fun build(configure: Builder.() -> Unit): WApi = builder.apply(configure).build()
    }
}
