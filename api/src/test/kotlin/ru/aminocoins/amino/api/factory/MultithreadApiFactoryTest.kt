package ru.aminocoins.amino.api.factory

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MultithreadApiFactoryTest {

    @Test
    fun `WHEN threads count is x THEN api http client engine config threads count is x`() {
        val x = 1
        val multithreadApiFactory = MultithreadApiFactory(x)
        assertTrue {
            multithreadApiFactory.api.mHttpClient.engine.config.threadsCount == x
        }
    }
}
