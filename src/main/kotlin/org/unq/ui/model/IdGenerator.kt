package org.unq.ui.model

class IdGenerator {
    private var userId = 0
    private var postId = 0
    private var commentId = 0

    fun nextUserId(): String = "u_${++userId}"
    fun nextPostId(): String = "p_${++postId}"
    fun nextCommentId(): String = "c_${++commentId}"
}
