package ru.aminocoins.amino.wapi.`object`.post

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.aminocoins.amino.api.Actions
import ru.aminocoins.amino.api.Api
import ru.aminocoins.amino.api.goodNarviiResponseOrThrow
import ru.aminocoins.amino.wapi.WApi
import ru.aminocoins.amino.wapi.`object`.ApiObject
import ru.aminocoins.amino.wapi.`object`.community.Community
import ru.aminocoins.amino.wapi.`object`.user.User
import java.util.*

@Suppress("unused")
data class Post(
    override val wApi: WApi,
    private val api: Api,
    val user: User,
    val community: Community,
    val id: String,
    val type: Type
) : ApiObject {

    suspend fun pay(count: Int) {
        api.postCommunityAction(
            action = if (type == Type.Blog) Actions.tippingBlog(id) else Actions.tippingItem(id),
            body = buildJsonObject {
                put("objectId", id)
                put("coins", count)
                put("tippingContext", buildJsonObject {
                    put("transactionId", UUID.randomUUID().toString())
                })
                put("objectType", if (type == Type.Blog) 1 else 2)
                put("timestamp", System.currentTimeMillis())
            },
            ndcId = community.id.toInt(),
            headers = mapOf(
                User.API_KEY_HEADER to User.API_KEY_VALUE_FORMAT.format(user.apiKey.string)
            )
        ).goodNarviiResponseOrThrow
    }

    enum class Type {
        Blog,
        Article
    }
}
