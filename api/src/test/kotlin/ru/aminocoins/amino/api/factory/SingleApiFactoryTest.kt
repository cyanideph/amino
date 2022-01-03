package ru.aminocoins.amino.api.factory

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

@Suppress("KotlinConstantConditions")
internal class SingleApiFactoryTest {

    private val factory = SingleApiFactory()

    @Test
    fun `WHEN api is instantiated THEN it have new reference`() {
        val instanceOne = factory.api
        val instanceTwo = factory.api
        assertFalse {
            instanceOne === instanceTwo
        }
    }
}
