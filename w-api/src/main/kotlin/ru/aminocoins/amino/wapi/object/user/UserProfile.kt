package ru.aminocoins.amino.wapi.`object`.user

import ru.aminocoins.amino.wapi.WApi
import ru.aminocoins.amino.wapi.`object`.ApiObject

@Suppress("unused")
data class UserProfile(
     override val wApi: WApi,
) : ApiObject
