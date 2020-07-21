package work.wtks.android.iidxresultmanager.ui.gallery

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import work.wtks.android.iidxresultmanager.R

abstract class ListItemRecyclerViewAdapter(private val context: Context) :
    ListAdapter<ListItem, ListItemRecyclerViewAdapter.ViewHolder>(ListItem.DIFF_CALLBACK) {

    protected abstract fun onResultClicked(id: Int)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListItemRecyclerViewAdapter.ViewHolder {
        when (viewType) {
            ListItem.VIEW_TYPE_CONTENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_gallery_list_item, parent, false)
                return ViewHolder(view).apply {
                    itemView.setOnClickListener {
                        val item = getItem(adapterPosition)
                        if (item is ListItem.Content) onResultClicked(item.result.id)
                    }
                }
            }
            ListItem.VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_gallery_list_header, parent, false)
                return ViewHolder(view)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: ListItemRecyclerViewAdapter.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ListItem.Header -> {
                val view = holder.itemView
                val dateView: TextView = view.findViewById(R.id.date)

                dateView.text = SimpleDateFormat.getDateInstance().format(item.date)
            }
            is ListItem.Content -> {
                val view = holder.itemView
                val imageView: ImageView = view.findViewById(R.id.result_image)
                val idView: TextView = view.findViewById(R.id.result_title)
                val optionsView: TextView = view.findViewById(R.id.result_options)
                val datetimeView: TextView = view.findViewById(R.id.result_datetime)

                val result = item.result
                Glide
                    .with(holder.itemView)
                    .load(result.getFile(context))
                    .centerCrop()
                    .into(imageView)
                idView.text = result.songInfo.title
                optionsView.text =
                    "${result.randomOption.displayText}${if (result.legacyOption == true) " LEGACY" else ""}"
                datetimeView.text =
                    SimpleDateFormat.getDateTimeInstance().format(result.date)
            }
        }
    }

    override fun getItemViewType(position: Int) = currentList[position].type

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}