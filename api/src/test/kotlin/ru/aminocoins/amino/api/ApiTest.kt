package ru.aminocoins.amino.api

import io.ktor.client.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ApiTest {

    private val emptyApi = Api("", HttpClient())

    @Test
    fun `WHEN action is global THEN url contains global word`() {
        val api = emptyApi
        val actionUrl = api.actionToUrl(Actions.DEVICE, global = true)
        assertTrue {
            actionUrl.encodedPath.contains(Words.PATH_GLOBAL)
        }
    }

    @Test
    fun `WHEN action is global AND has ndcId THEN url contains global word and has ndcId`() {
        val api = emptyApi
        val ndcId = 100500
        val actionUrl = api.actionToUrl(Actions.DEVICE, global = true, ndcId = ndcId)
        assertTrue {
            val encodedPath = actionUrl.encodedPath
            encodedPath.contains(Words.PATH_GLOBAL) && encodedPath.contains(ndcId.toString())
        }
    }

    @Test
    fun `WHEN action isn't global AND hasn't ndcId THEN throw IllegalArgumentException`() {
        val api = emptyApi
        assertThrows<IllegalArgumentException> {
            api.actionToUrl(Actions.DEVICE)
        }
    }
}
