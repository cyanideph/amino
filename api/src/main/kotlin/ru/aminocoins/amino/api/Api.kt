package ru.aminocoins.amino.api

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonObject
import ru.aminocoins.amino.api.Words.PATH_COMMUNITY
import ru.aminocoins.amino.api.Words.PATH_GLOBAL
import ru.aminocoins.amino.api.Words.PATH_SERVICE
import ru.aminocoins.amino.api.plugin.Plugin

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Api(
    val mBaseUrl: String,
    val mHttpClient: HttpClient,
) {

    init {
        addHeadersMapInterceptorToHttpClient()
    }

    private val headersMap: HashMap<String, String> = hashMapOf()

    private fun addHeadersMapInterceptorToHttpClient() {
        mHttpClient.requestPipeline.intercept(HttpRequestPipeline.Phases.Before) {
            context.apply {
                headersMap.forEach { (name, value) -> this.header(name, value) }
            }
        }
    }

    suspend fun getCommunityInfo(ndcId: Int, params: (HashMap<String, String>.() -> Unit)? = null) {
        return globalActionToUrl(Actions.GET_COMMUNITY_INFO, ndcId = ndcId, params = params).get()
    }

    suspend fun postDevice(body: JsonObject): JsonObject {
        return globalActionToUrl(Actions.POST_DEVICE).post(body)
    }

    suspend inline fun <reified TResponse> Url.get(): TResponse = mHttpClient.get(
        this
    )

    suspend inline fun <reified TResponse, TBody : Any> Url.post(body: TBody): TResponse = mHttpClient.post(
        this
    ) {
        this.body = body
    }

    fun communityActionToUrl(action: String, ndcId: Int, params: (HashMap<String, String>.() -> Unit)? = null): Url {
        val paramsMap = hashMapOf<String, String>().apply {
            params?.invoke(this)
        }
        return actionToUrl(action, ndcId = ndcId, paramsMap = paramsMap)
    }

    fun globalActionToUrl(
        action: String,
        ndcId: Int? = null,
        params: (HashMap<String, String>.() -> Unit)? = null,
    ): Url {
        val paramsMap = hashMapOf<String, String>().apply {
            params?.invoke(this)
        }
        return actionToUrl(action, global = true, ndcId = ndcId, paramsMap = paramsMap)
    }

    fun actionToUrl(
        action: String,
        global: Boolean = false,
        ndcId: Int? = null,
        paramsMap: Map<String, String> = emptyMap(),
    ): Url {
        val servicePath: String = if (global) {
            if (ndcId != null) {
                "$PATH_GLOBAL/$PATH_SERVICE-$PATH_COMMUNITY$ndcId"
            } else {
                "$PATH_GLOBAL/$PATH_SERVICE"
            }
        } else {
            if (ndcId == null) {
                throw IllegalArgumentException("ndcId can't to be null if request isn't global")
            }
            "$PATH_COMMUNITY$ndcId/$PATH_SERVICE"
        }
        val paramsString = StringBuilder().apply {
            paramsMap.forEach { (name, value) -> append("$name=$value&") }
            removeSuffix("&")
        }.toString()
        return Url("$mBaseUrl/$servicePath/$action?$paramsString")
    }

    fun <TPlugin : Any, TConfiguration : Any> install(
        plugin: Plugin<TPlugin, TConfiguration>,
        configure: TConfiguration.() -> Unit,
    ) {
        plugin.install(this, configure)
    }

    fun header(name: String, value: String) {
        headersMap[name] = value
    }

    fun remHeader(name: String) {
        headersMap.remove(name)
    }

    companion object {

        const val NARVII_BASE_URL = "https://service.narvii.com/api/v1"

        fun HttpClientConfig<*>.applyDefaultApiConfiguration() {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            expectSuccess = false
        }
    }
}
