package io.github.azusalad.isosta

import android.app.Application
import io.github.azusalad.isosta.data.AppContainer
import io.github.azusalad.isosta.data.DefaultAppContainer

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
