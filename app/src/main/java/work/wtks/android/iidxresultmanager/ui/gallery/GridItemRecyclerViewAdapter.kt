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

abstract class GridItemRecyclerViewAdapter(private val context: Context) :
    ListAdapter<ListItem, GridItemRecyclerViewAdapter.ViewHolder>(ListItem.DIFF_CALLBACK) {

    protected abstract fun onResultClicked(id: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            ListItem.VIEW_TYPE_CONTENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_gallery_grid_item, parent, false)
                return ViewHolder(view).apply {
                    itemView.setOnClickListener {
                        val item = getItem(adapterPosition)
                        if (item is ListItem.Content) onResultClicked(item.result.id)
                    }
                }
            }
            ListItem.VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_gallery_grid_header, parent, false)
                return ViewHolder(view)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (val item = getItem(position)) {
            is ListItem.Header -> {
                val view = holder.itemView
                val dateView: TextView = view.findViewById(R.id.date)

                dateView.text = SimpleDateFormat.getDateInstance().format(item.date)
            }
            is ListItem.Content -> {
                val view = holder.itemView
                val imageView: ImageView = view.findViewById(R.id.item_image)
                val contentView: TextView = view.findViewById(R.id.content)

                val result = item.result
                Glide
                    .with(holder.itemView)
                    .load(result.getFile(context))
                    .thumbnail(0.25f)
                    .centerCrop()
                    .into(imageView)
                contentView.text = result.songInfo.title
            }
        }
    }

    override fun getItemViewType(position: Int) = currentList[position].type

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}