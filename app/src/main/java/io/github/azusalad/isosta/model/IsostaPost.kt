package io.github.azusalad.isosta.model

data class IsostaPost (
    val poster: io.github.azusalad.isosta.model.IsostaUser,
    val postDescription: String,
    val mediaList: List<io.github.azusalad.isosta.model.PostMedia>,
    val commentList: List<io.github.azusalad.isosta.model.IsostaComment>,
    val postLink: String
)


