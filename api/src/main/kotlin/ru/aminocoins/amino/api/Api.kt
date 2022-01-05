package ru.aminocoins.amino.api

import io.ktor.client.*
import io.ktor.client.features.*
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

    suspend fun postGlobalAction(
        action: String,
        body: JsonObject,
        ndcId: Int? = null,
        params: (HashMap<String, String>.() -> Unit)? = null,
        headers: Map<String, String>? = null,
    ): JsonObject = globalActionToUrl(action, ndcId, params).post(body, headers)

    suspend fun getGlobalAction(
        action: String,
        ndcId: Int? = null,
        params: (HashMap<String, String>.() -> Unit)? = null,
        headers: Map<String, String>? = null,
    ): JsonObject = globalActionToUrl(action, ndcId, params).get(headers)

    suspend fun postCommunityAction(
        action: String,
        ndcId: Int,
        body: JsonObject,
        params: (HashMap<String, String>.() -> Unit)? = null,
        headers: Map<String, String>? = null,
    ): JsonObject = communityActionToUrl(action, ndcId, params).post(body, headers)

    suspend fun getCommunityAction(
        action: String,
        ndcId: Int,
        params: (HashMap<String, String>.() -> Unit)? = null,
        headers: Map<String, String>? = null,
    ): JsonObject = communityActionToUrl(action, ndcId, params).get(headers)

    suspend inline fun <reified TResponse> Url.get(headers: Map<String, String>? = null): TResponse = mHttpClient.get(
        this
    ) {
        headers?.forEach { (name, value) -> header(name, value) }
    }

    suspend inline fun <reified TResponse, TBody : Any> Url.post(
        body: TBody,
        headers: Map<String, String>? = null,
    ): TResponse = mHttpClient.post(
        this
    ) {
        this.body = body
        headers?.forEach { (name, value) -> header(name, value) }
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

    fun <TPlugin : Any, TConfiguration : Any> install(
        plugin: Plugin<TPlugin, TConfiguration>,
    ) {
        plugin.install(this) {}
    }

    fun header(name: String): String = headersMap.getOrDefault(name, "")

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
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
            expectSuccess = false
        }

        fun Api.applyDefaultApiConfiguration() {
            header(Words.HEADER_DEVICE_ID,
                "19dd04c001032ccafdcfdcb0e5928d26c7d1b49abb6d01771b33b775f1829eebc860c15ecbef9ff257")
        }
    }
}
