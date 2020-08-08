package com.mredrock.cyxbs.discover.map.ui.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mredrock.cyxbs.common.ui.BaseViewModelFragment
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.IconBean
import com.mredrock.cyxbs.discover.map.utils.MapLayout
import com.mredrock.cyxbs.discover.map.viewmodels.MapViewModel
import kotlinx.android.synthetic.main.map_fragment_main.*


class MainFragment : BaseViewModelFragment<MapViewModel>() {
    override val viewModelClass: Class<MapViewModel>
        get() = MapViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment_main, container, false)
    }

}