<img src="../images/amino_banner.jpeg" alt="Amino banner">

### UNSTABLE library for interact with Amino api

- UNSTABLE (only dynamic api objects)
- Based on Ktor
- Expandable (Has STABLE Plugins api)

---

# NOT RECOMMENDED FOR USE, USE W-API INSTEAD IF POSSIBLE

---

### How to use? An example

```kotlin
@ObsoleteCoroutinesApi
@DelicateCoroutinesApi
class MyApplication {

    private val mThreadsCount = 8
    private val mContext = newFixedThreadPoolContext(mThreadsCount, "My App Context")
    private val mContextExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleException(throwable)
    }

    @Test
    fun main() {
        GlobalScope.launch(mContext + mContextExceptionHandler) {
            someFunction()
        }
    }

    private suspend fun someFunction() {
        val api = MultithreadApiFactory(mThreadsCount = mThreadsCount, httpClientConfigure = {
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }).api
        val postDeviceBody = buildJsonObject {
            put("Some property", JsonPrimitive("Some value"))
            put("Some object", buildJsonObject {
                put("Some object property", JsonPrimitive("Some object value"))
            })
            put("Some array", buildJsonArray {
                add(JsonPrimitive("Some array value"))
            })
        }
        val postDeviceResponse = api.postDevice(postDeviceBody)
        val responseStatusCode = postDeviceResponse["api:statuscode"]!!.jsonPrimitive.int
        println("Response status code: $responseStatusCode")
    }

    private fun handleException(throwable: Throwable) {
        when (throwable) {
            is UnknownHostException -> println("No internet connection!")
            is Exception -> println("Unknown exception has occurred")
        }
        throwable.printStackTrace()
    }
}
```
