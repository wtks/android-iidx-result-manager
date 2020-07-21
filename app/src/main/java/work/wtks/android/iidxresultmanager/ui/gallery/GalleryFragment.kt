package work.wtks.android.iidxresultmanager.ui.gallery

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_gallery.*
import work.wtks.android.iidxresultmanager.R
import work.wtks.android.iidxresultmanager.ui.MainActivity

class GalleryFragment : Fragment() {
    private val viewModel: GalleryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        childFragmentManager.beginTransaction()
            .replace(R.id.content, GalleryListFragment::class.java.newInstance())
            .commit()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val activity = activity as AppCompatActivity
        activity.supportActionBar?.title = "IIDX Result Manager"
        activity.supportActionBar?.subtitle = ""
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        fab.setOnClickListener { (activity as MainActivity).launchPhotoChooser() }

        viewModel.viewType.observe(viewLifecycleOwner, Observer {
            when (it) {
                R.layout.fragment_gallery_grid -> childFragmentManager.beginTransaction()
                    .replace(R.id.content, GalleryGridFragment::class.java.newInstance())
                    .commit()
                else -> childFragmentManager.beginTransaction()
                    .replace(R.id.content, GalleryListFragment::class.java.newInstance())
                    .commit()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_gallery, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_gallery -> {
                viewModel.toggleViewType()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}