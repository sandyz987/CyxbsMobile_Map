package com.mredrock.cyxbs.discover.map.ui.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.ui.activity.adapter.AllPictureRvAdapter
import kotlinx.android.synthetic.main.map_fragment_all_picture.*


class AllPictureFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment_all_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        map_rv_all_picture.layoutManager = staggeredGridLayoutManager
        val allPictureAdapter = context?.let { AllPictureRvAdapter(it, mutableListOf()) }
        map_rv_all_picture.adapter = allPictureAdapter
    }
}