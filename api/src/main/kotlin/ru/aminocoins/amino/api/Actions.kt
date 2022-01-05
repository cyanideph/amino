package ru.aminocoins.amino.api

@Suppress("unused")
object Actions {

    const val DEVICE = "device"
    const val COMMUNITY_INFO = "community/info"
    const val LOGIN = "auth/login"
    const val LOGOUT = "auth/logout"
    const val ACCOUNT_AFFILIATIONS = "account/affiliations"

    fun tippingBlog(blogId: String) = "blog/$blogId/tipping"

    fun tippingItem(itemId: String) = "item/$itemId/tipping"
}
