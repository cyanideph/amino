package ru.aminocoins.amino.wapi.`object`.community

import ru.aminocoins.amino.api.Api
import ru.aminocoins.amino.wapi.WApi
import ru.aminocoins.amino.wapi.`object`.ApiObject
import ru.aminocoins.amino.wapi.`object`.post.Post
import ru.aminocoins.amino.wapi.`object`.user.User

@Suppress("unused")
data class Community(
    override val wApi: WApi,
    private val api: Api,
    val user: User,
    val id: String,
) : ApiObject {

    fun resolvePost(postId: String, postType: Post.Type): Post = Post(wApi, api, user, this, postId, postType)
}
