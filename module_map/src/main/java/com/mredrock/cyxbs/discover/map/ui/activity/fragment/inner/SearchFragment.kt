package com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mredrock.cyxbs.common.ui.BaseViewModelFragment
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.viewmodels.MapViewModel

class SearchFragment : BaseViewModelFragment<MapViewModel>() {
    override val viewModelClass = MapViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.map_fragment_search, container, false)
    }

}