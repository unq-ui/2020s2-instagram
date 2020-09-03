package org.unq.ui.model

import java.time.LocalDateTime

data class Comment(val id: String, val body: String, val user: User)
data class Post(val id: String, val user: User, var landscape: String, var portrait: String, var description: String, val date: LocalDateTime, val comments: MutableList<Comment>, val likes: MutableList<User>)
data class User(val id: String, var name: String, val email: String, var password: String, var image: String, val followers: MutableList<User>)

data class DraftPost(val portrait: String, val landscape: String, val description: String)
data class DraftComment(val body: String)
