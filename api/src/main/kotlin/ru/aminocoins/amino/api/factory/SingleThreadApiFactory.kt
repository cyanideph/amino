package ru.aminocoins.amino.api.factory

import io.ktor.client.*
import ru.aminocoins.amino.api.Api
import ru.aminocoins.amino.api.Api.Companion.applyDefaultApiConfiguration

class SingleThreadApiFactory(
    private val mBaseUrl: String = Api.NARVII_BASE_URL,
    private val httpClientConfigure: (HttpClientConfig<*>.() -> Unit)? = null
) : ApiFactory {

    private val httpClient: HttpClient
        get() = HttpClient {
            engine {
                threadsCount = 1
            }
            applyDefaultApiConfiguration()
            httpClientConfigure?.invoke(this)
        }
    override val api: Api
        get() = Api(mBaseUrl, httpClient).apply {
            applyDefaultApiConfiguration()
        }
}
