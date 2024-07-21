package com.example.network

import com.example.isosta.model.Thumbnail
import org.jsoup.Jsoup


// Needs to be open to override in the FakeIsostaApiService test.
open class IsostaApiService {
    // Asynchronous function so internet call works in the background
    open suspend fun getThumbnailPhotos(url: String = "https://imginn.com/hood_anya/"): List<Thumbnail> {
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
            // The src might be not be https source but instead //assets... so only take the https srcs
            if (imageSrc[0] == 'h') {
                println("LOG: the imagesrc is: $imageSrc")
                val newThumbnail = Thumbnail(picture = imageSrc, text = imageText)
                thumbnailList.add(newThumbnail)
            }
        }
        return thumbnailList
    }
}

// Create a singleton object
//object IsostaApi {
//    val IsostaService : IsostaApiService by lazy {
//        IsostaApiService()
//    }
//}
