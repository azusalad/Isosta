package io.github.azusalad.isosta

import android.app.Application
import io.github.azusalad.isosta.data.AppContainer
import io.github.azusalad.isosta.data.DefaultAppContainer

// The application object is attached to the application container
class IsostaApplication : Application() {

    companion object {
        const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36"
    }

    // Stores the default app container object.  lateinit because the container variable
    // will be initialized when onCreate() is called
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }

}
