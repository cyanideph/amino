package ru.aminocoins.amino.api.plugin

import ru.aminocoins.amino.api.Api

interface Plugin<TPlugin : Any, out TConfiguration : Any> {

    fun install(api: Api, configure: TConfiguration.() -> Unit) : TPlugin
}
