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

    data class DeleteBulletinResponse(
        val code: Int,
        val message: String
    )

    data class UpdateBulletinResponse(
        val code: Int,
        val message: String
    )

    data class ReportResponse(
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

    data class DeleteBulletin(
        val id_freeboard: Int
    )

    data class UpdateBulletin(
        val id_freeboard: Int,
        val freeboard_title: String,
        val freeboard_content: String,
        val username: String
    )

    data class SendReportData(
        val report_content: String,
        val bulletin_id: Int,
        val reporter_email: String
    )

}
