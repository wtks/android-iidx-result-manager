package work.wtks.android.iidxresultmanager.ui.viewer

import android.content.Intent
import android.icu.text.SimpleDateFormat.getDateTimeInstance
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.davemorrissey.labs.subscaleview.ImageSource
import kotlinx.android.synthetic.main.viewer_page_fragment.*
import kotlinx.android.synthetic.main.viewer_page_fragment.fab
import work.wtks.android.iidxresultmanager.R
import java.text.SimpleDateFormat

class ViewerPageFragment : Fragment() {
    companion object {
        const val ARG_RESULT_ID = "result_id"
    }

    private val viewModel: ViewerPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.viewer_page_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val activity = activity as AppCompatActivity

        arguments?.takeIf { it.containsKey(ARG_RESULT_ID) }?.apply {
            viewModel.resultId.value = getInt(ARG_RESULT_ID)
        }
        viewModel.imageUri.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                imageView.setImage(ImageSource.uri(it))
            }
        })
        viewModel.result.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                text_title.text =
                    "${it.songInfo.title} (${it.songInfo.difficulty.name[0]})"
                text_datetime.text = getDateTimeInstance().format(it.date)
                text_options.text =
                    "${it.randomOption.displayText}${if (it.legacyOption == true) " LEGACY" else ""}"
                text_memo.text = it.memo
            }
        })

        fab.setOnClickListener { startShareIntent() }

        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_viewer, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                startShareIntent()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startShareIntent() {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    requireContext(),
                    "work.wtks.fileprovider",
                    viewModel.result.value!!.getFile(requireContext())
                )
            )
            type = "image/jpeg"
        }
        startActivity(
            Intent.createChooser(
                shareIntent,
                resources.getText(R.string.action_share)
            )
        )
    }
}