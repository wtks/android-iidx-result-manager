package work.wtks.android.iidxresultmanager.data

import android.content.Context
import work.wtks.android.iidxresultmanager.data.valueobject.Difficulty

object DataCSVReader {
    data class SongData(
        val version: String,
        val title: String,
        val genre: String,
        val artist: String,
        val levels: Map<Difficulty, Int>
    )

    fun read(context: Context): List<SongData> {
        val assertManager = context.assets
        val result = mutableListOf<SongData>()
        var first = true
        assertManager.open("song_data.csv").bufferedReader().forEachLine {
            if (first) {
                first = false
                return@forEachLine
            }

            val rowData = it.split(",")
            result.add(
                SongData(
                    version = rowData[0],
                    title = rowData[1],
                    genre = rowData[2],
                    artist = rowData[3],
                    levels = mapOf(
                        Difficulty.BEGINNER to rowData[5].toInt(),
                        Difficulty.NORMAL to rowData[12].toInt(),
                        Difficulty.HYPER to rowData[19].toInt(),
                        Difficulty.ANOTHER to rowData[26].toInt(),
                        Difficulty.LEGGENDARIA to rowData[33].toInt()
                    ).filterValues { v -> v != 0 }
                )
            )
        }
        return result
    }

}