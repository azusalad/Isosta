package io.github.azusalad.network

import io.github.azusalad.isosta.model.IsostaComment
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.model.IsostaPost
import io.github.azusalad.isosta.model.PostMedia
import io.github.azusalad.isosta.model.Thumbnail
import org.jsoup.Jsoup


// Needs to be open to override in the FakeIsostaApiService test.
open class IsostaApiService {
    private val hostName = "https://imginn.com"

    open suspend fun getUserInfo(url: String = "https://imginn.com/suisei.daily.post/"): IsostaUser {
        // Initialize the list to add the thumbnails to
        val thumbnailList = arrayListOf<Thumbnail>()
        // Fetch the website with user agent so we don't get forbidden page
        val doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get()
        println("INFO->IsostaApiService.kt: The doc is " + doc)

        // Get user information
        // The index at which these stats show on the website
        val postCountIndex = 0
        val followerCountIndex = 1
        val followingCountIndex = 2

        val userInfo = doc.getElementsByClass("userinfo")[0]
        val profilePicture = userInfo.getElementsByTag("img")[0].attr("src")
        val profileName = userInfo.getElementsByTag("h1")[0].text()
        val profileHandle = userInfo.getElementsByTag("h2")[0].text()
        val profileDescription = userInfo.getElementsByClass("bio")[0].text()
        val statsInfo = userInfo.getElementsByClass("num")
        val postCount = statsInfo[postCountIndex].text()
        val followerCount = statsInfo[followerCountIndex].text()
        val followingCount = statsInfo[followingCountIndex].text()

        // Load all items
        val allInfo = doc.getElementsByClass("item")
        println("INFO->IsostaApiService.kt: allInfo is " + allInfo)
        for (i in allInfo) {
            // Get all src url of images
            var imageSrc = i.getElementsByTag("img").attr("src")
            if (imageSrc[0] != 'h') {
                imageSrc = i.getElementsByTag("img").attr("data-src")
            }
            println("LOG->IsostaApiService.kt: the imageSrc is " + imageSrc)
            // Get the information text for the thumbnail.  Cut out the alt part that describes the picture.
            // Like "May be an image containing text"
            val imageText = i.getElementsByTag("img").attr("alt")
            println("LOG->IsostaApiService.kt: the imageText is " + imageSrc)
            // Get the post link
            val postLink = hostName + i.getElementsByTag("a").attr("href")
            println("LOG->IsostaApiService.kt: the postLink is " + postLink)
            // The src might be not be https source but instead //assets... so only take the https srcs
            if (imageSrc[0] == 'h') {
                println("LOG->IsostaApiService.kt: the new imageSrc is: $imageSrc")
                val newThumbnail = Thumbnail(
                    picture = imageSrc,
                    text = imageText,
                    postLink = postLink,
                    sourceHandle = profileHandle,
                    date = System.currentTimeMillis() / 1000L
                )
                thumbnailList.add(newThumbnail)
                println("LOG->IsostaApiService.kt: Adding new thumbnail to list")
            }
        }

        return IsostaUser(
            profilePicture = profilePicture,
            profileName = profileName,
            profileHandle = profileHandle,
            profileLink = url,
            profileDescription = profileDescription,
            postCount = postCount,
            followerCount = followerCount,
            followingCount = followingCount,
            thumbnailList = thumbnailList
        )
    }

    // Get the post information
    open suspend fun getPostInfo(url: String): IsostaPost {
        println("LOG: getPostInfo() called")

        // Initialize the media list to add pictures to
        val mediaList = arrayListOf<PostMedia>()
        val commentList = arrayListOf<IsostaComment>()
        // Fetch the website with user agent so we don't get forbidden page
        val doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get()
        println("INFO: The doc of the post: " + doc)

        // Get the poster's information
        val posterInfo = doc.getElementsByClass("user")[0]
        println("INFO: posterInfo = " + posterInfo)
        val posterProfileName = posterInfo.getElementsByClass("fullname")[0]
            .getElementsByTag("h1")[0].text()
        println("LOG: posterProfileName = " + posterProfileName)
        val posterProfileHandle = posterInfo.getElementsByClass("username")[0]
            .getElementsByTag("h2")[0].text()
        println("LOG: posterProfileHandle = " + posterProfileHandle)
        val posterProfilePicture = posterInfo.getElementsByTag("img")[0].attr("src")
        println("LOG: posterProfilePicture = " + posterProfilePicture)
        val posterProfileLink = hostName + posterInfo.getElementsByTag("a")[0].attr("href")
        println("LOG: posterProfileLink = " + posterProfileLink)
        val poster = IsostaUser(
            profileName = posterProfileName,
            profileHandle = posterProfileHandle,
            profilePicture = posterProfilePicture,
            profileLink = posterProfileLink
        )

        // Get media information
        // There might only be one media
        var allMediaInfo = doc.getElementsByClass("swiper-wrapper")
        if (allMediaInfo.size > 0) {
            allMediaInfo = allMediaInfo[0].getElementsByTag("img")
        }
        else {
            allMediaInfo = doc.getElementsByClass("media-wrap")[0].getElementsByTag("img")
        }
        println("INFO: allMediaInfo = " + allMediaInfo)
        for (i in allMediaInfo) {
            var imageSrc = i.getElementsByTag("img").attr("src")
            // The first two images will have the link under the src attribute, however the
            // other images will have a placeholder lazy image under the src attribute.
            // The data-src attribute seems to have the correct link
            if (imageSrc[0] != 'h') {
                imageSrc = i.getElementsByTag("img").attr("data-src")
            }
            if (imageSrc == "") {
                continue
            }
            println("LOG: imageSrc = " + imageSrc)
            val imageText = i.getElementsByTag("img").attr("alt")
            println("LOG: imageText = " + imageText)
            val newMedia =
                PostMedia(
                    mediaSrc = imageSrc,
                    mediaText = imageText
                )
            mediaList.add(newMedia)
        }

        // Get description
        // Description might not exist
        val postDescriptionInfo = doc.getElementsByClass("desc")
        val postDescription: String
        if (postDescriptionInfo.size > 0) {
            postDescription = postDescriptionInfo[0].text()
        }
        else {
            postDescription = ""
        }
        println("LOG: postDescription = " + postDescription)

        // Get comments
        var allCommentsInfo = doc.getElementsByClass("comments")  // Might be no comments
        if (allCommentsInfo.size > 0) {
            allCommentsInfo = allCommentsInfo[0].getElementsByClass("comment")
            println("INFO: allCommentsInfo = " + allCommentsInfo)
            for (i in allCommentsInfo) {
                var commentProfilePicture = i.getElementsByTag("img")[0].attr("src")
                if (commentProfilePicture[0] != 'h') {
                    commentProfilePicture = i.getElementsByTag("img")[0].attr("data-src")
                }
                println("LOG: commentProfilePicture = " + commentProfilePicture)
                val commentProfileLink = hostName + i.getElementsByClass("con")[0].getElementsByTag("a")[0].attr("href")
                println("LOG: commentProfileLink = " + commentProfileLink)
                val commentProfileHandle = i.getElementsByClass("con")[0].getElementsByTag("h3")[0].text()
                println("LOG: commentProfileHandle = " + commentProfileHandle)
                val commentText = i.getElementsByClass("con")[0].getElementsByTag("p")[0].text()
                println("LOG: commentText = " + commentText)
                val newComment = IsostaComment(
                    commentText = commentText,
                    user = IsostaUser(
                        profilePicture = commentProfilePicture,
                        profileLink = commentProfileLink,
                        profileHandle = commentProfileHandle,
                        profileName = "Unknown"
                    )
                )
                commentList.add(newComment)
            }
        }

        // Combine all information and return
        println("LOG: getPostInfo() completed")
        return IsostaPost(
            commentList = commentList,
            mediaList = mediaList,
            poster = poster,
            postDescription = postDescription,
            postLink = url
        )
    }

    // Search for a user
    open suspend fun getSearchInfo(query: String): List<IsostaUser> {
        val url = hostName + "/search?q=" + query.replace(" ","+")
        val userList = arrayListOf<IsostaUser>()

        println("LOG->IsostaApiService.kt: Performing network request with url: " + url)
        val doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get()
        println("INFO->IsostaApiService.kt: The doc of the post: " + doc)

        val allUserInfo = doc.getElementsByClass("tab-item user-item")
        for (user in allUserInfo) {
            var profilePicture = user.getElementsByTag("img").attr("src")
            // The first two images will have the link under the src attribute, however the
            // other images will have a placeholder lazy image under the src attribute.
            // The data-src attribute seems to have the correct link
            if (profilePicture[0] != 'h') {
                profilePicture = user.getElementsByTag("img").attr("data-src")
            }
            if (profilePicture == "") {
                continue
            }
            println("LOG->IsostaApiService.kt: profilePicture = " + profilePicture)

            val profileName = user.getElementsByClass("fullname")[0].getElementsByTag("span").text()
            println("LOG->IsostaApiService.kt: profileName = " + profileName)
            val profileHandle = user.getElementsByClass("username")[0].text()
            println("LOG->IsostaApiService.kt: profileHandle = " + profileHandle)
            val profileLink = hostName + user.attr("href")
            println("LOG->IsostaApiService.kt: profileLink = " + profileLink)

            userList.add(
                IsostaUser(
                    profilePicture = profilePicture,
                    profileName = profileName,
                    profileHandle = profileHandle,
                    profileLink = profileLink
                )
            )
        }
        return userList
    }
}

// Create a singleton object
//object IsostaApi {
//    val IsostaService : IsostaApiService by lazy {
//        IsostaApiService()
//    }
//}
