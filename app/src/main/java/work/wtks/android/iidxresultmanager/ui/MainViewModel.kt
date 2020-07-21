package work.wtks.android.iidxresultmanager.ui

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import java.io.File
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var tempImageFile: File? = null
        private set

    fun initTempImageFile(): Uri {
        val image = File.createTempFile(
            UUID.randomUUID().toString(),
            ".jpg",
            getApplication<Application>().externalCacheDir
        )
        image.deleteOnExit()
        tempImageFile = image
        return image.toUri()
    }
}