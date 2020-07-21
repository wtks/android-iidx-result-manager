package work.wtks.android.iidxresultmanager.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import work.wtks.android.iidxresultmanager.R
import work.wtks.android.iidxresultmanager.ui.viewer.ViewerPageFragment

class GalleryGridFragment : Fragment() {

    private val viewModel: GalleryViewModel by activityViewModels()

    private lateinit var gridAdapter: GridItemRecyclerViewAdapter
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery_grid, container, false)

        gridLayoutManager = GridLayoutManager(context, 4)
        gridAdapter = object : GridItemRecyclerViewAdapter(requireContext()) {
            override fun onResultClicked(id: Int) {
                findNavController().navigate(
                    R.id.action_galleryFragment_to_viewerPageFragment,
                    Bundle().apply {
                        putInt(ViewerPageFragment.ARG_RESULT_ID, id)
                    })
            }
        }

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = gridLayoutManager
                adapter = gridAdapter
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listItems.observe(viewLifecycleOwner, Observer {
            gridAdapter.submitList(it)
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.positionIndex = gridLayoutManager.findFirstVisibleItemPosition()
    }

    override fun onResume() {
        super.onResume()
        gridLayoutManager.scrollToPosition(viewModel.positionIndex)
    }
}