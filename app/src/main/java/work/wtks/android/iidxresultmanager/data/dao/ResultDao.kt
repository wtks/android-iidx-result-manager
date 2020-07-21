package work.wtks.android.iidxresultmanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import work.wtks.android.iidxresultmanager.data.entity.Result

@Dao
interface ResultDao {
    @Query("SELECT * FROM results ORDER BY date DESC")
    fun getAll(): LiveData<List<Result>>

    @Query("SELECT * FROM results WHERE id = :id")
    suspend fun get(id: Int): Result?

    @Insert
    suspend fun insert(result: Result): Long
}