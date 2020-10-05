# UNQ » UIs » TP Instagram

[![JitPack](https://jitpack.io/v/unq-ui/2020s2-instagram.svg)](https://jitpack.io/#unq-ui/2020s2-instagram)

Instagram es una plataforma donde los usuarios suben fotos y pueden comentar las fotos ajenas.

## Especificación de Dominio

### Dependencia

Agregar el repositorio:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Agregar la dependencia:

```xml
<dependency>
    <groupId>com.github.unq-ui</groupId>
    <artifactId>2020s2-instagram</artifactId>
    <version>1.0.0</version>
</dependency>
```

Toda interacción con el dominio se hace a través de la clase `InstagramSystem`. La programación del dominio ya es proveído por la cátedra.

Para utilizar el system con información existen dos maneras.

Puede generar de cero la instancia y agregar la información que necesite:

```kotlin
import org.ui.model.InstagramSystem
import org.ui.model.DraftPost

val system = InstagramSystem()

val a1 = system.register("Name 1","name1@gmail.com", "pass1","https://pix.example/1.png")
val a2 = system.register("Name 2","name2@gmail.com", "pass2","https://pix.example/2.png")
val a3 = system.register("Name 3","name3@gmail.com", "pass3","https://pix.example/3.png")

system.addPost(a1.id, DraftPost("https://imgageLink.com/portrait1.png", "https://imgageLink.com/landscape1.png", "description1"))
system.addPost(a2.id, DraftPost("https://imgageLink.com/portrait2.png", "https://imgageLink.com/landscape2.png", "description2"))
system.addPost(a3.id, DraftPost("https://imgageLink.com/portrait3.png", "https://imgageLink.com/landscape3.png", "description3"))
```

O puede utilizar el _system_ pre-cargado por la cátedra:

```kotlin
import org.ui.bootstrap.getInstagramSystem

val system = getInstagramSystem()
```

### `InstagramSystem`

```kotlin

// @Throw NotFound si `email` o `password` son incorrectos
fun login(email: String, password: String): User

// @Throw UsedEmail si `email` ya está en uso
fun register(name: String, email: String, password: String, image: String): User

// @Throw NotFound si `userId` no existe
fun getUser(userId: String): User

// @Throw NotFound si `postId` no existe
fun getPost(postId: String): Post

// @Throw NotFound si `userId` no existe
fun editProfile(userId: String, name: String, password: String, image: String): User

// @Throw NotFound si `userId` no existe
fun addPost(userId: String, draftPost: DraftPost): Post

// @Throw NotFound si `postId` no existe
fun editPost(postId: String, draftPost: DraftPost): Post

fun deletePost(postId: String): Unit

// @Throw NotFound si `postId` o `fromUserId` no existen
fun addComment(postId: String, fromUserId: String, draftComment: DraftComment): Post

// @Throw NotFound si `postId` o `userId` no existen
fun updateLike(postId: String, userId: String): Post

// @Throw NotFound si `postId` o `userId` no existen
fun updateFollower(fromUserId: String, toUserId: String): Unit

// @Throw NotATag si el tag parametro no contine `#`
fun searchByTag(tag: String): List<Post>

fun searchByUserName(name: String): List<Post>

fun searchByUserId(userId: String): List<Post>

fun searchByName(name: String): List<User>

// @Throw NotFound si `userId` no existe
fun timeline(userId: String): List<Post>
```

### User

Es el usuario del sistema.

```kotlin
data class User(
    val id: String,
    var name: String,
    val email: String,
    var password: String,
    var image: String,
    val followers: MutableList<User>
)
```

### Post

Es el post ya creado

```kotlin
data class Post(
    val id: String,
    val user: User,
    var landscape: String,
    var portrait: String,
    var description: String,
    val date: LocalDateTime,
    val comments: MutableList<Comment>,
    val likes: MutableList<User>
)
```

### DraftPost

Es el post antes de ser guardado por el sistema

```kotlin
data class DraftPost(
    val portrait: String,
    val landscape: String,
    val description: String
)
```

### Comment

Es el comentario ya creado

```kotlin
data class Comment(
    val id: String,
    val body: String,
    val user: User
)
```

### `DraftComment`

El comentario antes de ser guardado por el sistema.

```kotlin
data class DraftComment(val body: String)
```
