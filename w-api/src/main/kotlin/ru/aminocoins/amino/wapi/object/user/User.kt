package ru.aminocoins.amino.wapi.`object`.user

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import ru.aminocoins.amino.api.Actions
import ru.aminocoins.amino.api.Api
import ru.aminocoins.amino.api.goodNarviiResponseOrThrow
import ru.aminocoins.amino.wapi.WApi
import ru.aminocoins.amino.wapi.`object`.ApiObject
import ru.aminocoins.amino.wapi.`object`.community.Community
import java.util.*

@Suppress("unused")
data class User(
    override val wApi: WApi,
    private val api: Api,
    val id: String,
    val login: String,
    var apiKey: ApiKey,
) : ApiObject {

    fun resolveCommunity(id: String): Community = Community(wApi, api, this, id)

    suspend fun myCommunities(): Set<Community> {
        val jsonObject = api.getGlobalAction(Actions.ACCOUNT_AFFILIATIONS, params = {
            put("type", "active")
        }, headers = mapOf(
            API_KEY_HEADER to API_KEY_VALUE_FORMAT.format(apiKey.string)
        )).goodNarviiResponseOrThrow
        val communityLinkedList: LinkedList<String> = LinkedList()
        jsonObject["affiliations"]!!.jsonArray.forEach {
            communityLinkedList.add(it.jsonPrimitive.content)
        }
        return communityLinkedList.map {
            Community(wApi, api, this, it)
        }.toSet()
    }

    fun serialize(): String = buildJsonObject {
        put("id", id)
        put("login", login)
        put("apiKey", Json.encodeToJsonElement(apiKey))
    }.toString()

    @Serializable
    data class ApiKey(
        val string: String,
        val validToInSeconds: Long,
        val refreshToken: String,
    ) {

        val valid: Boolean
            get() = validToInSeconds > (System.currentTimeMillis() / 1000)
    }

    companion object {
        const val API_KEY_HEADER = "NDCAUTH"
        const val API_KEY_VALUE_FORMAT = "sid=%s"
    }
}
