package ru.aminocoins.amino.signer

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import ru.aminocoins.amino.api.Api
import ru.aminocoins.amino.api.plugin.Plugin

private const val HEADER_SIGNATURE = "NDC-MSG-SIG"

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Signer(
    private val configuration: Configuration,
) {

    data class Configuration(
        var apiPath: String = "api/v1/",
        var baseUrl: String = "https://domain.com/",
        var signaturePath: String = "signature",
        var apiKeyHeaderName: String = "securityKey",
        var apiKey: String = "",
        var responseConvertStrategy: (response: JsonObject) -> String = {
            it["d"]!!.jsonPrimitive.content
        },
        var requestConvertStrategy: (dataForSignature: String) -> Any = {
            buildJsonObject {
                put("d", it)
            }
        },
    ) {

        val signatureUrl: String
            get() = "$baseUrl$apiPath$signaturePath"
    }

    fun interceptBaseUrl(baseUrlForIntercept: String, httpClient: HttpClient) {
        httpClient.requestPipeline.intercept(HttpRequestPipeline.Before) {
            if (!context.url.buildString().startsWith(baseUrlForIntercept) || context.method != HttpMethod.Post) {
                return@intercept
            }
            val jsonStrBody = context.body.toString()
            context.header(HEADER_SIGNATURE, calculateSignature(httpClient, jsonStrBody))
        }
    }

    suspend fun calculateSignature(httpClient: HttpClient, data: String): String {
        val jsonResponse: JsonObject = httpClient.post(configuration.signatureUrl) {
            body = configuration.requestConvertStrategy.invoke(data)
            header(configuration.apiKeyHeaderName, configuration.apiKey)
        }
        return configuration.responseConvertStrategy.invoke(jsonResponse)
    }

    companion object ApiPlugin : Plugin<Signer, Configuration> {
        override fun install(api: Api, configure: Configuration.() -> Unit): Signer {
            val configuration = Configuration().apply(configure)
            val signer = Signer(configuration)
            signer.interceptBaseUrl(api.mBaseUrl, api.mHttpClient)
            return signer
        }

    }
}
