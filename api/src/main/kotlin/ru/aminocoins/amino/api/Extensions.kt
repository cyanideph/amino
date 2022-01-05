package ru.aminocoins.amino.api

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

val JsonObject.goodNarviiResponse: Boolean
    get() = getOrDefault("api:statuscode", JsonPrimitive(-1)).jsonPrimitive.int == 0

val JsonObject.narviiMessage: String
    get() = getOrDefault("api:message", JsonPrimitive("")).jsonPrimitive.content

val JsonObject.goodNarviiResponseOrThrow: JsonObject
    get() = if (goodNarviiResponse) this else throw IllegalStateException(narviiMessage)
