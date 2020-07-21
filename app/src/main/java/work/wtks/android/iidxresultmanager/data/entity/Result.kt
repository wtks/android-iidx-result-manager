package work.wtks.android.iidxresultmanager.data.entity

import android.content.Context
import android.os.Environment
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import work.wtks.android.iidxresultmanager.data.valueobject.RandomOption
import java.io.File
import java.util.*

@Entity(tableName = "results")
data class Result(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @Embedded
    val songInfo: SongInfo,
    val randomOption: RandomOption,
    val legacyOption: Boolean?,
    val date: Date,
    val memo: String
) {
    fun getFile(context: Context): File {
        return File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "$id.jpg"
        )
    }
}