package work.wtks.android.iidxresultmanager.ui.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import work.wtks.android.iidxresultmanager.R
import work.wtks.android.iidxresultmanager.data.DB
import work.wtks.android.iidxresultmanager.data.entity.Result
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    val viewType: MutableLiveData<Int> = MutableLiveData(R.layout.fragment_gallery_grid)

    val results: LiveData<List<Result>> = DB.getDatabase(application).resultDao().getAll()
    val listItems = Transformations.map(results) {
        val items = mutableListOf<ListItem>()
        var currentDate = Instant.ofEpochSecond(0L).atOffset(ZoneOffset.ofHours(9)).truncatedTo(ChronoUnit.DAYS)
        for (result in it) {
            val resultDate = result.date.toInstant().atOffset(ZoneOffset.ofHours(9)).truncatedTo(ChronoUnit.DAYS)
            if (currentDate != resultDate) {
                items.add(ListItem.Header(result.date))
                currentDate = resultDate
            }
            items.add(ListItem.Content(result))
        }
        return@map items as List<ListItem>
    }

    var positionIndex: Int = 0

    fun toggleViewType() {
        if (viewType.value == R.layout.fragment_gallery_grid) {
            viewType.value = R.layout.fragment_gallery_list
        } else {
            viewType.value = R.layout.fragment_gallery_grid
        }
    }
}