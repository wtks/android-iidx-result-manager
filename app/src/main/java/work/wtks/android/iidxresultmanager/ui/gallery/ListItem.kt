package work.wtks.android.iidxresultmanager.ui.gallery

import androidx.recyclerview.widget.DiffUtil
import work.wtks.android.iidxresultmanager.data.entity.Result
import java.util.*

sealed class ListItem {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem) = oldItem == newItem
            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                if (oldItem is Header && newItem is Header) {
                    return oldItem.date == newItem.date
                }
                if (oldItem is Content && newItem is Content) {
                    return oldItem.result.id == newItem.result.id
                }
                return false
            }
        }

        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_CONTENT = 2
    }

    abstract val type: Int

    class Header(val date: Date) : ListItem() {
        override val type: Int
            get() = VIEW_TYPE_HEADER
    }

    class Content(val result: Result) : ListItem() {
        override val type: Int
            get() = VIEW_TYPE_CONTENT
    }
}