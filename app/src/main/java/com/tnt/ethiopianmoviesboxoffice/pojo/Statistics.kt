package com.tnt.ethiopianmoviesboxoffice.pojo

import com.google.firebase.database.Exclude

class Statistics(
    var commentCount: String? = null,
    var dislikeCount: String? = null,
    var favoriteCount: String? = null,
    var likeCount: String? = null,
    var viewCount: String? = null

) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "commentCount" to commentCount,
            "dislikeCount" to dislikeCount,
            "favoriteCount" to favoriteCount,
            "likeCount" to likeCount,
            "viewCount" to viewCount
        )
    }

}