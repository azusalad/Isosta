package io.github.azusalad.isosta.model

data class IsostaPost (
    val poster: IsostaUser,
    val postDescription: String,
    val mediaList: List<PostMedia>,
    val commentList: List<IsostaComment>,
    val postLink: String
)


