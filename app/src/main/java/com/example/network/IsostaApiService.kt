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
        val allMediaInfo = doc.getElementsByClass("swiper-wrapper")[0]
            .getElementsByTag("img")
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
        val postDescription = doc.getElementsByClass("desc")[0].text()
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
