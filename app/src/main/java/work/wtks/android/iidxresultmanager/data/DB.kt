package work.wtks.android.iidxresultmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import work.wtks.android.iidxresultmanager.data.dao.ResultDao
import work.wtks.android.iidxresultmanager.data.entity.Result

@Database(
    entities = [
        Result::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DB : RoomDatabase() {

    abstract fun resultDao(): ResultDao

    companion object {
        @Volatile
        private var INSTANCE: DB? = null

        fun getDatabase(context: Context): DB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DB::class.java,
                    "iidx_result_manager.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}