package work.wtks.android.iidxresultmanager.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import work.wtks.android.iidxresultmanager.R
import work.wtks.android.iidxresultmanager.ui.viewer.ViewerPageFragment

class GalleryListFragment : Fragment() {

    private val viewModel: GalleryViewModel by activityViewModels()

    private lateinit var gridAdapter: ListItemRecyclerViewAdapter
    private lateinit var listLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery_list, container, false)

        gridAdapter = object : ListItemRecyclerViewAdapter(requireContext()) {
            override fun onResultClicked(id: Int) {
                findNavController().navigate(
                    R.id.action_galleryFragment_to_viewerPageFragment,
                    Bundle().apply {
                        putInt(ViewerPageFragment.ARG_RESULT_ID, id)
                    })
            }
        }
        listLayoutManager = LinearLayoutManager(context)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = listLayoutManager
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
        viewModel.positionIndex = listLayoutManager.findFirstVisibleItemPosition()
    }

    override fun onResume() {
        super.onResume()
        listLayoutManager.scrollToPosition(viewModel.positionIndex)
    }
}