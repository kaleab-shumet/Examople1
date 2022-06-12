package com.tnt.ethiopianmoviesboxoffice.pojo

class Item(
    var image: String? = null,
    var publishTime: String? = null,
    var statistics: Statistics? = null,
    var thumbnailUrl: String? = null,
    var title: String? = null,
    var videoId: String? = null,
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "image" to image,
            "publishTime" to publishTime,
            "statistics" to statistics,
            "thumbnailUrl" to thumbnailUrl,
            "title" to title,
            "videoId" to videoId
        )
    }


}