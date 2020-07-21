package work.wtks.android.iidxresultmanager.ui.register

import android.app.Application
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import work.wtks.android.iidxresultmanager.data.DB
import work.wtks.android.iidxresultmanager.data.entity.Result
import work.wtks.android.iidxresultmanager.data.entity.SongInfo
import work.wtks.android.iidxresultmanager.data.valueobject.Difficulty
import work.wtks.android.iidxresultmanager.data.valueobject.RandomOption
import java.io.File
import java.util.*

class RegisterResultViewModel(application: Application) : AndroidViewModel(application) {

    val songTitle: MutableLiveData<String> = MutableLiveData("")
    val songTitleIdx: MutableLiveData<Int> = MutableLiveData(0)
    val difficulty: MutableLiveData<Difficulty> = MutableLiveData(Difficulty.ANOTHER)
    val randomOption: MutableLiveData<RandomOption> = MutableLiveData(RandomOption.NULL)
    val legacyOption: MutableLiveData<Boolean> = MutableLiveData(false)
    val memo: MutableLiveData<String> = MutableLiveData("")

    val photoUri: MutableLiveData<Uri?> = MutableLiveData()
    val dateTime: MutableLiveData<Date?> = MutableLiveData()

    private val _isProcessing: MutableLiveData<Boolean> = MutableLiveData(false)
    val isProcessing: LiveData<Boolean> get() = _isProcessing
    val isValid: LiveData<Boolean> = Transformations.map(photoUri) { it != null }
    val isOkButtonEnabled: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        val okButtonObserver = Observer<Boolean> {
            val isProcessing = isProcessing.value ?: false
            val isValid = isValid.value ?: false
            isOkButtonEnabled.value =  !isProcessing && isValid
        }
        isOkButtonEnabled.addSource(isProcessing, okButtonObserver)
        isOkButtonEnabled.addSource(isValid, okButtonObserver)
    }

    fun registerResult() {
        viewModelScope.launch {
            _isProcessing.value = true
            withContext(Dispatchers.IO) {
                val id = DB.getDatabase(getApplication()).resultDao().insert(
                    Result(
                        0,
                        SongInfo(
                            songTitle.value!!,
                            difficulty.value!!
                        ),
                        randomOption.value!!,
                        legacyOption.value!!,
                        dateTime.value!!,
                        memo.value!!
                    )
                )

                val context = getApplication<Application>()

                File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "$id.jpg"
                ).outputStream().use { out ->
                    context.contentResolver.openInputStream(photoUri.value!!)!!.use {
                        it.copyTo(out)
                    }
                }
            }
            _isProcessing.value = false
        }
    }
}