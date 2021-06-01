package com.gatheringandyou.gandyoudemo.bulletinboards

import java.util.*

class DataCollection {

    data class GetCommentsResponse(
        val data: MutableList<GetCommentsData>,
        val code: Int,
        val message: String
    )

    data class SendCommentsResponse(
        val code: Int,
        val message: String
    )

    data class DeleteCommentsResponse(
        val code: Int,
        val message: String
    )

    data class GetCommentsData(
        val id_comments: Int,
        val user_id: Int,
        val user_email: String,
        val user_nickname: String,
        val comments_content: String,
        val comments_date: Date
    )

    data class SendCommentsData(
        val user_id: Int,
        val user_email: String,
        val user_nickname: String,
        val comments_content: String,
        val comments_date: String,
        val freeboard_id: Int
    )

    data class PostFreeBoardId(
        val freeboard_id: Int
    )


    data class DeleteCommentsData(
        val id_comments: Int
    )
}
