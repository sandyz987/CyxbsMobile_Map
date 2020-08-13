package com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel


class SearchHistoryFragment : Fragment() {
    private lateinit var viewModel: MapViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment_search_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)

    }


}