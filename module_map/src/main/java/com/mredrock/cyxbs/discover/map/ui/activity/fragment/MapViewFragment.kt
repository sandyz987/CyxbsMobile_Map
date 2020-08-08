package com.mredrock.cyxbs.discover.map.ui.activity.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mredrock.cyxbs.common.ui.BaseViewModelFragment
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.IconBean
import com.mredrock.cyxbs.discover.map.utils.MapLayout
import com.mredrock.cyxbs.discover.map.viewmodels.MapViewModel
import kotlinx.android.synthetic.main.map_activity_map.*
import kotlinx.android.synthetic.main.map_fragment_main.*
import kotlinx.android.synthetic.main.map_fragment_map.*


class MapViewFragment : BaseViewModelFragment<MapViewModel>() {
    override val viewModelClass: Class<MapViewModel>
        get() = MapViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.map_fragment_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        map_layout.addIcon(IconBean(0, 2945f, 6526f, 2430f, 3500f, 6210f, 6830f))
        map_layout.addIcon(IconBean(1, 2830f, 7488f, 2430f, 3250f, 7245f, 7717f))
        map_layout.setMyOnIconClickListener(object : MapLayout.OnIconClickListener {
            override fun onIconClick(v: View) {
                val bean = v.tag as IconBean
                map_layout.focusToPoint(bean.sx, bean.sy)
            }

        })
    }

}