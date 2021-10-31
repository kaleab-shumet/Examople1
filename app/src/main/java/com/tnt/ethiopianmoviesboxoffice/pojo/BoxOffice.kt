package com.tnt.ethiopianmoviesboxoffice.pojo

data class BoxOffice (
    val yearMovies: List<YearMovies>? = null,
    val createdTime: Long? = null,
    var monthMovies: List<MonthMovies>? = null,
    val uuid: String? = null
)