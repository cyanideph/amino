package ru.aminocoins.amino.wapi

import ru.aminocoins.amino.api.factory.MultithreadApiFactory

class GracefulWApiBuilder : WApi.Builder {
    override var threadsCount: Int = 8

    override fun build(): WApi {
        val api = MultithreadApiFactory(mThreadsCount = threadsCount).api
        return WApi(api)
    }
}
