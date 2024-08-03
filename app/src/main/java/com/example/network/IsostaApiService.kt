package com.example.network

import com.example.isosta.R
import com.example.isosta.model.IsostaComment
import com.example.isosta.model.IsostaUser
import com.example.isosta.model.IsostaPost
import com.example.isosta.model.PostMedia
import com.example.isosta.model.Thumbnail
import org.jsoup.Jsoup


// Needs to be open to override in the FakeIsostaApiService test.
open class IsostaApiService {
        // Asynchronous function so internet call works in the background
    open suspend fun getThumbnailPhotos(url: String = "https://imginn.com/suisei.daily.post/"): List<Thumbnail> {
        val hostName = "https://imginn.com"
        // Initialize the list to add the thumbnails to
        val thumbnailList = arrayListOf<Thumbnail>()
        // Fetch the website with user agent so we don't get forbidden page
        val doc = Jsoup.connect(url).userAgent("Mozilla").get()
        // Load all items
        val allInfo = doc.getElementsByClass("item")
        // Print this to logcat to make sure its working
        //println(allInfo)
        println(doc)

        for (i in allInfo) {
            // Get all src url of images
            val imageSrc = i.getElementsByTag("img").attr("src")
            // Get the information text for the thumbnail.  Cut out the alt part that describes the picture.
            // Like "May be an image containing text"
            val imageText = i.getElementsByTag("img").attr("alt").split(".")[0]
            // Get the post link
            val postLink = hostName + i.getElementsByTag("a").attr("href")
            // The src might be not be https source but instead //assets... so only take the https srcs
            if (imageSrc[0] == 'h') {
                println("LOG: the imagesrc is: $imageSrc")
                val newThumbnail = Thumbnail(picture = imageSrc, text = imageText, postLink = postLink)
                thumbnailList.add(newThumbnail)
            }
        }
        return thumbnailList
    }

    open suspend fun getUserInfo(url: String = "https://imginn.com/suisei.daily.post/"): IsostaUser {
        // Same as getThumbnailPhotos
        // TODO: Do something about these two functions like merge them
        val hostName = "https://imginn.com"
        val thumbnailList = arrayListOf<Thumbnail>()
        val doc = Jsoup.connect(url).userAgent("Mozilla").get()
        val allInfo = doc.getElementsByClass("item")
        for (i in allInfo) {
            val imageSrc = i.getElementsByTag("img").attr("src")
            val imageText = i.getElementsByTag("img").attr("alt").split(".")[0]
            val postLink = hostName + i.getElementsByTag("a").attr("href")
            if (imageSrc[0] == 'h') {
                println("LOG: the imagesrc is: $imageSrc")
                val newThumbnail = Thumbnail(picture = imageSrc, text = imageText, postLink = postLink)
                thumbnailList.add(newThumbnail)
            }
        }

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

        return IsostaUser(
            profilePicture = profilePicture,
            profileName = profileName,
            profileHandle = profileHandle,
            profileLink = url,
            profileDescription = profileDescription,
            postCount = postCount,
            followerCount = followerCount,
            followingCount = followingCount
        )
    }

    // Get the post information
    open suspend fun getPostInfo(url: String = "https://imginn.com/p/C-DJ4Z0hSjy"): IsostaPost {
        println("LOG: getPostInfo() called")
        val hostName = "https://imginn.com"

        // Initialize the media list to add pictures to
        val mediaList = arrayListOf<PostMedia>()
        val commentList = arrayListOf<IsostaComment>()
        // Fetch the website with user agent so we don't get forbidden page
        val doc = Jsoup.connect(url).userAgent("Mozilla").get()
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
            profileLink = posterProfileLink)

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
            val newMedia = PostMedia(mediaSrc = imageSrc, mediaText = imageText)
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
}

// Create a singleton object
//object IsostaApi {
//    val IsostaService : IsostaApiService by lazy {
//        IsostaApiService()
//    }
//}
