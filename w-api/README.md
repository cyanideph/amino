<img src="../images/amino_banner.jpeg" alt="Amino banner">

### STABLE library wrap of :api

- STABLE (static api objects and api)
- Expandable (Has STABLE Plugins api)

### How to use? Example

```kotlin
// A GOOD WAY TO USE W-API ASYNC IS APPLICATION COROUTINE CONTEXT

val wApi = WApi.build {
    threadsCount *= 2
}

runBlocking(Dispatchers.IO) {
    val user = wApi.authByEmail("email", "secret")
    val community = user.resolveCommunity("2")
    val post = community.resolvePost("postId", Post.Type.Blog)
    for (i in 1..100) {
        async {
            post.pay(500)
        }.start()
    }
}

// A GOOD WAY TO USE W-API ASYNC IS APPLICATION COROUTINE CONTEXT
```
