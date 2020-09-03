package org.unq.ui.bootstrap

import org.unq.ui.model.DraftComment
import org.unq.ui.model.InstagramSystem
import kotlin.random.Random

val photos = getPhotos()
val comments = getComments()
val users = getUsers()
val random = Random(1001)

private fun addUsers(instagramSystem: InstagramSystem) {
    users.forEach {
        instagramSystem.register(it.name, it.email, it.password, it.image)
    }
}

private fun addPhotos(instagramSystem: InstagramSystem) {
    instagramSystem.users.forEach { user ->
        val photos = List(15) { photos[random.nextInt(1, photos.size - 1)] }.toSet().toList()
        photos.forEach { instagramSystem.addPost(user.id, it) }
    }
}

private fun addFollowers(instagramSystem: InstagramSystem) {
    instagramSystem.users.forEach { user ->
        val selectedUsers = List(15) { instagramSystem.users[random.nextInt(0, instagramSystem.users.size - 1)].id }.toSet().toMutableList()
        selectedUsers.removeIf { user.id == it }
        selectedUsers.forEach { instagramSystem.updateFollower(user.id, it) }
    }
}

private fun addComments(instagramSystem: InstagramSystem) {
    instagramSystem.posts.forEach { post ->
        val selectedComments = List(7) { comments[random.nextInt(0, comments.size - 1)] }.toSet().toList()
        selectedComments.forEach {
            val user = instagramSystem.users[random.nextInt(0, instagramSystem.users.size - 1)]
            instagramSystem.addComment(post.id, user.id, DraftComment(it))
        }
    }
}

fun getInstagramSystem(): InstagramSystem {
    val instagramSystem = InstagramSystem()
    addUsers(instagramSystem)
    addPhotos(instagramSystem)
    addFollowers(instagramSystem)
    addComments(instagramSystem)
    return instagramSystem
}
