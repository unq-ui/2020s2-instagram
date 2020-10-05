package org.unq.ui.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class InstagramSystemTest {

    private fun getInstagramSystemWithTwoUsers(): InstagramSystem {
        val instagramSystem = InstagramSystem()
        instagramSystem.register("juan", "juan@gmail.com", "juan", "http://image.com/1234")
        instagramSystem.register("lean", "lean@gmail.com", "lean", "http://image.com/1234")
        return instagramSystem
    }

    private fun getInstagramSystemWithTwoUsersAndOnePostPerUser(): InstagramSystem {
        val instagramSystem = InstagramSystem()
        instagramSystem.register("juan", "juan@gmail.com", "juan", "http://image.com/1234")
        instagramSystem.register("lean", "lean@gmail.com", "lean", "http://image.com/1234")
        instagramSystem.addPost("u_1", DraftPost("landscape.png", "portrait.png", "#description"))
        Thread.sleep(1)
        instagramSystem.addPost("u_2", DraftPost("2landscape.png", "2portrait.png", "2description"))
        return instagramSystem
    }

    @Test
    fun registerTest() {
        val instagramSystem = InstagramSystem()
        assertEquals(instagramSystem.users.size, 0)
        instagramSystem.register("juan", "juan@gmail.com", "juan", "http://image.com/1234")
        assertEquals(instagramSystem.users.size, 1)
        val user = instagramSystem.users[0]
        assertEquals(user.id, "u_1")
        assertEquals(user.name, "juan")
        assertEquals(user.email, "juan@gmail.com")
        assertEquals(user.password, "juan")
        assertEquals(user.image, "http://image.com/1234")
        assertEquals(user.followers.size, 0)
    }

    @Test
    fun registerTwoTimesTest() {
        val instagramSystem = InstagramSystem()
        assertEquals(instagramSystem.users.size, 0)
        instagramSystem.register("juan", "juan@gmail.com", "juan", "http://image.com/1234")
        assertEquals(instagramSystem.users.size, 1)
        assertFailsWith<UsedEmail> {
            instagramSystem.register(
                "Juan",
                "juan@gmail.com",
                "juan",
                "http://image.com/1234"
            )
        }
    }

    @Test
    fun loginWithoutUserTest() {
        val instagramSystem = InstagramSystem()
        assertEquals(instagramSystem.users.size, 0)
        assertFailsWith<NotFound> { instagramSystem.login("juan@gmail.com", "juan") }
    }

    @Test
    fun loginTest() {
        val instagramSystem = InstagramSystem()
        assertEquals(instagramSystem.users.size, 0)
        instagramSystem.register("juan", "juan@gmail.com", "juan", "http://image.com/1234")
        assertEquals(instagramSystem.users.size, 1)
        val user = instagramSystem.login("juan@gmail.com", "juan")
        assertEquals(user.id, "u_1")
        assertEquals(user.name, "juan")
        assertEquals(user.email, "juan@gmail.com")
        assertEquals(user.password, "juan")
        assertEquals(user.image, "http://image.com/1234")
        assertEquals(user.followers.size, 0)
    }

    @Test
    fun editProfileTest() {
        val instagramSystem = getInstagramSystemWithTwoUsers()
        val user = instagramSystem.editProfile("u_1", "juan2", "juan2", "http://image.com/4321")
        assertEquals(user.id, "u_1")
        assertEquals(user.name, "juan2")
        assertEquals(user.email, "juan@gmail.com")
        assertEquals(user.password, "juan2")
        assertEquals(user.image, "http://image.com/4321")
        assertEquals(user.followers.size, 0)
    }

    @Test
    fun editProfileWithWrongUserId() {
        val instagramSystem = getInstagramSystemWithTwoUsers()
        assertFailsWith<NotFound> { instagramSystem.editProfile("u_1000", "juan2", "juan2", "http://image.com/4321") }
    }

    @Test
    fun addPostTest() {
        val instagramSystem = getInstagramSystemWithTwoUsers()
        assertEquals(instagramSystem.posts.size, 0)
        instagramSystem.addPost("u_1", DraftPost("portrait.png", "landscape.png", "description"))
        assertEquals(instagramSystem.posts.size, 1)
        val post = instagramSystem.posts[0]
        assertEquals(post.id, "p_1")
        assertEquals(post.user.id, "u_1")
        assertEquals(post.landscape, "landscape.png")
        assertEquals(post.portrait, "portrait.png")
        assertEquals(post.description, "description")
        assertEquals(post.likes.size, 0)
    }

    @Test
    fun addPostWithWronUserIdTest() {
        val instagramSystem = getInstagramSystemWithTwoUsers()
        assertFailsWith<NotFound> {
            instagramSystem.addPost(
                "u_10000",
                DraftPost("landscape.png", "portrait.png", "description")
            )
        }
    }

    @Test
    fun editPostTest() {
        val instagramSystem = getInstagramSystemWithTwoUsers()
        assertEquals(instagramSystem.posts.size, 0)
        instagramSystem.addPost("u_1", DraftPost("portrait.png", "landscape.png", "description"))
        assertEquals(instagramSystem.posts.size, 1)
        instagramSystem.editPost("p_1", DraftPost("portrait2.png", "landscape2.png", "description2"))
        val post = instagramSystem.posts[0]
        assertEquals(post.id, "p_1")
        assertEquals(post.user.id, "u_1")
        assertEquals(post.landscape, "landscape2.png")
        assertEquals(post.portrait, "portrait2.png")
        assertEquals(post.description, "description2")
        assertEquals(post.likes.size, 0)
    }

    @Test
    fun editPostWithWrongPostIdTest() {
        val instagramSystem = getInstagramSystemWithTwoUsers()
        assertEquals(instagramSystem.posts.size, 0)
        instagramSystem.addPost("u_1", DraftPost("landscape.png", "portrait.png", "description"))
        assertEquals(instagramSystem.posts.size, 1)
        assertFailsWith<NotFound> {
            instagramSystem.editPost(
                "p_10000",
                DraftPost("landscape2.png", "portrait2.png", "description2")
            )
        }
    }

    @Test
    fun deletePostTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        assertEquals(instagramSystem.posts.size, 2)
        instagramSystem.deletePost("p_1")
        assertEquals(instagramSystem.posts.size, 1)
    }

    @Test
    fun deletePostTestWithWrongId() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        assertEquals(instagramSystem.posts.size, 2)
        instagramSystem.deletePost("p_1000")
        assertEquals(instagramSystem.posts.size, 2)
    }

    @Test
    fun addCommentTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val post = instagramSystem.addComment("p_1", "u_2", DraftComment("comment"))
        assertEquals(post.id, "p_1")
        assertEquals(post.comments.size, 1)
        val comment = post.comments[0]
        assertEquals(comment.user.id, "u_2")
        assertEquals(comment.body, "comment")
    }

    @Test
    fun addCommentWithWrongUserIdTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        assertFailsWith<NotFound> { instagramSystem.addComment("p_1", "u_20000", DraftComment("comment")) }
    }

    @Test
    fun addCommentWithWrongPostIdTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        assertFailsWith<NotFound> { instagramSystem.addComment("p_1000", "u_2", DraftComment("comment")) }
    }

    @Test
    fun updateLikeTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val originalPost = instagramSystem.getPost("p_1")
        assertEquals(originalPost.likes.size, 0)
        val post = instagramSystem.updateLike("p_1", "u_2")
        assertEquals(post.likes.size, 1)
    }

    @Test
    fun updateLikeTwoTimesTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val originalPost = instagramSystem.getPost("p_1")
        assertEquals(originalPost.likes.size, 0)
        val firstTimePost = instagramSystem.updateLike("p_1", "u_2")
        assertEquals(firstTimePost.likes.size, 1)
        val secondTimePost = instagramSystem.updateLike("p_1", "u_2")
        assertEquals(secondTimePost.likes.size, 0)
    }

    @Test
    fun updateLikeWithWrongPostIdTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        assertFailsWith<NotFound> { instagramSystem.updateLike("p_10000", "u_2") }
    }

    @Test
    fun updateLikeWithWrongUserIdTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        assertFailsWith<NotFound> { instagramSystem.updateLike("p_1", "u_20000") }
    }

    @Test
    fun updateFollowerTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val originalUser = instagramSystem.getUser("u_1")
        assertEquals(originalUser.followers.size, 0)
        instagramSystem.updateFollower("u_1", "u_2")
        assertEquals(originalUser.followers.size, 1)
    }

    @Test
    fun updateFollowerTwoTimesTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val originalUser = instagramSystem.getUser("u_1")
        assertEquals(originalUser.followers.size, 0)
        instagramSystem.updateFollower("u_1", "u_2")
        assertEquals(originalUser.followers.size, 1)
        instagramSystem.updateFollower("u_1", "u_2")
        assertEquals(originalUser.followers.size, 0)
    }

    @Test
    fun updateFollowerWithWrongFromUserIdTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        assertFailsWith<NotFound> { instagramSystem.updateFollower("u_10000", "u_2") }
    }

    @Test
    fun updateFollowerWithWrongToUserIdTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        assertFailsWith<NotFound> { instagramSystem.updateFollower("u_1", "u_20000") }
    }

    @Test
    fun searchByTagTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val posts = instagramSystem.searchByTag("#description")
        assertEquals(posts.size, 1)
        val post = posts[0]
        assertEquals(post.user.id, "u_1")
    }

    @Test
    fun searchByTagWithWrongTagFormatTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        assertFailsWith<NotATag> { instagramSystem.searchByTag("description") }
    }

    @Test
    fun searchByUserNameTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val posts = instagramSystem.searchByUserName("a")
        assertEquals(posts.size, 2)
        assertEquals(posts[0].user.id, "u_2")
        assertEquals(posts[1].user.id, "u_1")
    }

    @Test
    fun searchByUserIdTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val posts = instagramSystem.searchByUserId("u_1")
        assertEquals(posts.size, 1)
        assertEquals(posts[0].user.id, "u_1")
    }

    @Test
    fun timelineTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        instagramSystem.updateFollower("u_1", "u_2")
        val posts = instagramSystem.timeline("u_1")
        assertEquals(posts.size, 1)
        assertEquals(posts[0].user.id, "u_2")
    }

    @Test
    fun timelineWithWrongUserIdTest() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        assertFailsWith<NotFound> { instagramSystem.timeline("u_1000") }
    }

    @Test
    fun searchByName() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val result = instagramSystem.searchByName("ju")
        assertEquals(result.size, 1)
        assertEquals(result[0].name, "juan")
    }

    @Test
    fun searchByNameWithoutName() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val result = instagramSystem.searchByName("")
        assertEquals(result.size, 0)
    }

    @Test
    fun searchByNameOnlySpacesName() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val result = instagramSystem.searchByName("   ")
        assertEquals(result.size, 0)
    }

    @Test
    fun searchByNameWithSameLetter() {
        val instagramSystem = getInstagramSystemWithTwoUsersAndOnePostPerUser()
        val result = instagramSystem.searchByName("a")
        assertEquals(result.size, 2)
    }
}
