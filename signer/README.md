<img src="../images/amino_banner.jpeg" alt="Amino banner">

### API plugin for sign post requests by rest api

### How to use? Example

```kotlin
val wApi = WApi.build {
    threadsCount *= 2
}.apply {
    install(Signer) {
        baseUrl = "https://myreapidomain.com/"
        apiPath = "my/reapi/path/"
        apiKeyHeaderName = "myApiKeyHeader"
        apiKey = "myApiKey"
        signaturePath = "my/signature/path"
        requestConvertStrategy = {
            buildJsonObject {
                put("dataForSignature", it)
            }
        }
        responseConvertStrategy = {
            it["signature"]!!.jsonPrimitive.content
        }
    }
}
```
