package com.mredrock.cyxbs.discover.map.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.ui.adapter.AllPictureRvAdapter
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel
import kotlinx.android.synthetic.main.map_fragment_all_picture.*


class AllPictureFragment : Fragment() {
    private lateinit var viewModel: MapViewModel
    private lateinit var allPictureAdapter: AllPictureRvAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment_all_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        map_rv_all_picture.layoutManager = staggeredGridLayoutManager
        allPictureAdapter = AllPictureRvAdapter(requireContext(), mutableListOf())
        map_rv_all_picture.adapter = allPictureAdapter

        map_iv_all_picture_back.setOnClickListener {
            viewModel.fragmentAllPictureIsShowing.value = false
        }


    }

    override fun onResume() {
        super.onResume()
        if (viewModel.placeDetails.value?.images != null) {
            allPictureAdapter.setList(viewModel.placeDetails.value?.images!!)
        }

    }
}