package com.tnt.ethiopianmoviesboxoffice.pojo

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
class BoxOffice (
    var createdTime: Long = 0,
    var items: List<Item>? = null,
    var itemsSize: Int = 0,
    var season: String? = null,
) {

    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "createdTime" to createdTime,
            "items" to items,
            "itemsSize" to itemsSize,
            "season" to season
        )
    }


    }