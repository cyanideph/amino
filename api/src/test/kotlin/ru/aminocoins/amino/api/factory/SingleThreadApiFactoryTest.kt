package ru.aminocoins.amino.api.factory

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SingleThreadApiFactoryTest {

    private val defaultFactory = SingleThreadApiFactory()

    @Test
    fun `WHEN api is instantiated THEN it http client config threads count is 1`() {
        assertTrue {
            defaultFactory.api.mHttpClient.engine.config.threadsCount == 1
        }
    }
}
