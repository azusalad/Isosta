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
            val postLink = i.getElementsByTag("a").attr("href")
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
        val hostName = "https://imginn.com"
        // Intialize the media list to add pictures to
        val mediaList = arrayListOf<PostMedia>()
        val commentList = arrayListOf<IsostaComment>()
        // Fetch the website with user agent so we don't get forbidden page
        val doc = Jsoup.connect(url).userAgent("Mozilla").get()
        print("The doc of the post: " + doc)
        // Get the poster's information
        val posterInfo = doc.getElementsByClass("user")[0]
        print("LOG: posterInfo = " + posterInfo)
        val posterProfileName = posterInfo.getElementsByClass("fullname")[0]
            .getElementsByTag("h1")[0].text()
        print("LOG: posterProfileName = " + posterProfileName)
        val posterProfileHandle = posterInfo.getElementsByClass("username")[0]
            .getElementsByTag("h1")[0].text()
        print("LOG: posterProfileHandle = " + posterProfileHandle)
        val posterProfilePicture = posterInfo.getElementsByTag("img")[0].attr("src")
        print("LOG: posterProfilePicture = " + posterProfilePicture)
        val posterProfileLink = hostName + posterInfo.getElementsByTag("a")[0].attr("href")
        print("LOG: posterProfileLink = " + posterProfileLink)
        val poster = IsostaUser(
            profileName = posterProfileName,
            profileHandle = posterProfileHandle,
            profilePicture = posterProfilePicture,
            profileLink = posterProfileLink)
        // Get media information
        val allMediaInfo = doc.getElementsByClass("swiper-wrapper")[0]
            .getElementsByTag("img")
        print("LOG: allMediaInfo = " + allMediaInfo)
        for (i in allMediaInfo) {
            val imageSrc = i.getElementsByTag("img").attr("src")
            print("LOG: imageSrc = " + imageSrc)
            val imageText = i.getElementsByTag("img").attr("alt")
            print("LOG: imageText = " + imageText)
            val newMedia = PostMedia(mediaSrc = imageSrc, mediaText = imageText)
            mediaList.add(newMedia)
        }
        // Get description
        val postDescription = doc.getElementsByClass("desc")[0].text()
        print("LOG: postDescription = " + postDescription)
        // Get comments
        val allCommentsInfo = doc.getElementsByClass("comments")[0].getElementsByClass("comment")
        print("LOG: allCommentsInfo = " + allCommentsInfo)
        for (i in allCommentsInfo) {
            val commentProfilePicture = i.getElementsByTag("img")[0].attr("src")
            print("LOG: commentProfilePicture = " + commentProfilePicture)
            val commentProfileLink = hostName + i.getElementsByClass("con")[0].getElementsByTag("a")[0].attr("href")
            print("LOG: commentProfileLink = " + commentProfileLink)
            val commentProfileHandle = i.getElementsByClass("con")[0].getElementsByTag("h3")[0].text()
            print("LOG: commentProfileHandle = " + commentProfileHandle)
            val commentText = i.getElementsByClass("con")[0].getElementsByTag("p")[0].text()
            print("LOG: commentText = " + commentText)
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
        // Combine all information and return
        print("LOG: getPostInfo() completed")
        return IsostaPost(
            commentList = commentList,
            mediaList = mediaList,
            poster = poster,
            postDescription = postDescription
        )
    }
}

// Create a singleton object
//object IsostaApi {
//    val IsostaService : IsostaApiService by lazy {
//        IsostaApiService()
//    }
//}
