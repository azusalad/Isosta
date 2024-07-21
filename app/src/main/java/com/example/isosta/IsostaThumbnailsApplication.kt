package com.example.isosta

import android.app.Application
import com.example.isosta.data.AppContainer
import com.example.isosta.data.DefaultAppContainer

// The application object is attached to the application container
class IsostaThumbnailsApplication : Application() {

    // Stores the default app container object.  lateinit because the container variable
    // will be initialized when onCreate() is called
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }

}
