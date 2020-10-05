package org.unq.ui.model

import java.time.LocalDateTime

class InstagramSystem(val users: MutableList<User> = mutableListOf(), val posts: MutableList<Post> = mutableListOf(), private val idGenerator: IdGenerator = IdGenerator()) {

    fun login(email: String, password: String): User {
        return users.find { it.email == email && it.password == password } ?: throw NotFound("User")
    }

    fun register(name: String, email: String, password: String, image: String): User {
        existUserWithEmail(email)
        val user = User(idGenerator.nextUserId(), name, email, password, image, mutableListOf())
        users.add(user)
        return user
    }

    fun getUser(userId: String): User = users.find { it.id == userId } ?: throw NotFound("User")

    fun getPost(postId: String): Post = posts.find { it.id == postId } ?: throw NotFound("Post")

    fun editProfile(userId: String, name: String, password: String, image: String): User {
        val user = getUser(userId)
        user.name = name
        user.password = password
        user.image = image
        return user
    }

    fun addPost(userId: String, draftPost: DraftPost): Post {
        val user = getUser(userId)
        val post = Post(idGenerator.nextPostId(), user, draftPost.landscape, draftPost.portrait, draftPost.description, LocalDateTime.now(), mutableListOf(), mutableListOf())
        posts.add(post)
        return post
    }

    fun editPost(postId: String, draftPost: DraftPost): Post {
        val post = getPost(postId)
        post.description = draftPost.description
        post.landscape = draftPost.landscape
        post.portrait = draftPost.portrait
        return post
    }

    fun deletePost(postId: String) {
        posts.removeIf { it.id == postId }
    }

    fun addComment(postId: String, fromUserId: String, draftComment: DraftComment): Post {
        val user = getUser(fromUserId)
        val post = getPost(postId)
        post.comments.add(Comment(idGenerator.nextCommentId(), draftComment.body, user))
        return post
    }

    fun updateLike(postId: String, userId: String): Post {
        val user = getUser(userId)
        val post = getPost(postId)
        if (post.likes.contains(user)) {
            post.likes.remove(user)
            return post
        }
        post.likes.add(user)
        return post
    }

    fun updateFollower(fromUserId: String, toUserId: String) {
        val fromUser = getUser(fromUserId)
        val toUser = getUser(toUserId)
        if (fromUser.followers.contains(toUser)) {
            fromUser.followers.remove(toUser)
        } else {
            fromUser.followers.add(toUser)
        }
    }

    fun searchByTag(tag: String): List<Post> {
        if (!tag.startsWith("#")) throw NotATag()
        return posts.filter { it.description.contains(tag) }.sortedByDescending { it.date }
    }

    fun searchByUserName(name: String): List<Post> {
        val userIds = users.filter { it.name.contains(name, true) }
        return posts.filter { userIds.contains(it.user) }.sortedByDescending { it.date }
    }

    fun searchByUserId(userId: String): List<Post> {
        val user = getUser(userId)
        return posts.filter { it.user == user }.sortedByDescending { it.date }
    }

    fun searchByName(name: String): List<User> {
        if (name.isBlank()) return listOf()
        return users.filter { it.name.contains(name, true) }.sortedBy { it.name }
    }

    fun timeline(userId: String): List<Post> {
        val user = getUser(userId)
        return posts.filter { user.followers.contains(it.user) }.sortedByDescending { it.date }
    }

    private fun existUserWithEmail(email: String) {
        if (users.any { it.email == email }) throw UsedEmail()
    }
}
