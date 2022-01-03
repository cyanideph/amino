package ru.aminocoins.amino.api.factory

import io.ktor.client.*
import ru.aminocoins.amino.api.Api
import ru.aminocoins.amino.api.Api.Companion.applyDefaultApiConfiguration

class SingleApiFactory(
    private val mBaseUrl: String = Api.NARVII_BASE_URL,
    private val mHttpClient: HttpClient = HttpClient {
        applyDefaultApiConfiguration()
    }
) : ApiFactory {
    override val api: Api
        get() = Api(mBaseUrl, mHttpClient)
}
