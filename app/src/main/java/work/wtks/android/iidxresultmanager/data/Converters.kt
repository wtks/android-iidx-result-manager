package work.wtks.android.iidxresultmanager.data

import androidx.room.TypeConverter
import work.wtks.android.iidxresultmanager.data.valueobject.Difficulty
import work.wtks.android.iidxresultmanager.data.valueobject.PlayMode
import work.wtks.android.iidxresultmanager.data.valueobject.RandomOption
import java.util.*

class Converters {
    @TypeConverter
    fun fromRandomOptionToString(v: RandomOption) = v.name

    @TypeConverter
    fun fromStringToRandomOption(v: String) = RandomOption.valueOf(v)

    @TypeConverter
    fun fromPlayModeToString(v: PlayMode) = v.name

    @TypeConverter
    fun fromStringToPlayMode(v: String) = PlayMode.valueOf(v)

    @TypeConverter
    fun fromDifficultyToString(v: Difficulty) = v.name

    @TypeConverter
    fun fromStringToDifficulty(v: String) = Difficulty.valueOf(v)

    @TypeConverter
    fun fromTimestamp(value: Long?) = if (value == null) null else Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date?) = date?.time
}