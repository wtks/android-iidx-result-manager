package work.wtks.android.iidxresultmanager.ui.viewer

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import work.wtks.android.iidxresultmanager.data.DB

class ViewerPageViewModel(application: Application) : AndroidViewModel(application) {
    val resultId: MutableLiveData<Int> = MutableLiveData()

    val result = Transformations.switchMap(resultId) { id ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            val result = DB.getDatabase(getApplication()).resultDao().get(id)
            emit(result)
        }
    }
    val imageUri = Transformations.map(result) {
        if (it == null) {
            return@map null
        } else {
            return@map it.getFile(getApplication()).toUri()
        }
    }

}