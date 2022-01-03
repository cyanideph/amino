<img src="images/amino_banner.jpeg" alt="Amino banner">

## Includes modules:
- API — not wrapped library for interact with Amino. NOT RECOMMENDED TO USE, USE W-API INSTEAD IF POSSIBLE
- W-API — wrap of api library for interact with Amino.
- SIGNER — api plugin to sign post requests to Amino.

## Gradle dependencies
Firstly use maven publish plugin to publish all to maven local repository.

Then use next snippets in your build script:

### Kotlin

**API**
```kotlin
implementation("ru.aminocoins.amino:api:$api")
```
**W-API**
```kotlin
implementation("ru.aminocoins.amino:w-api:$wApi")
```
**SIGNER**
```kotlin
imlementation("ru.aminocoins.amino:signer:$signer")
```
