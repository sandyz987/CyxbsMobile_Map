package com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mredrock.cyxbs.common.utils.extensions.gone
import com.mredrock.cyxbs.common.utils.extensions.invisible
import com.mredrock.cyxbs.common.utils.extensions.visible
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.IconBean
import com.mredrock.cyxbs.discover.map.bean.PlaceItem
import com.mredrock.cyxbs.discover.map.component.MapLayout
import com.mredrock.cyxbs.discover.map.ui.activity.adapter.SymbolRvAdapter
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel
import kotlinx.android.synthetic.main.map_fragment_map_view.*


class MapViewFragment : Fragment() {
    private lateinit var viewModel: MapViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private val placeData = mutableListOf<PlaceItem>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment_map_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
        /**
         * 初始化地图view
         */
        viewModel.mapInfo.observe(viewLifecycleOwner, Observer { data ->
            placeData.clear()
            placeData.addAll(data.placeList)
            val list = data.placeList
            val iconList = mutableListOf<IconBean>()
            list.forEach { bean ->
                val buildingList = bean.buildingList
                buildingList.forEach { building ->
                    iconList.add(IconBean(bean.placeId.toInt(),
                            bean.placeCenterX.toFloat(),
                            bean.placeCenterY.toFloat(),
                            building.buildingLeft.toFloat(),
                            building.buildingRight.toFloat(),
                            building.buildingTop.toFloat(),
                            building.buildingBottom.toFloat(),
                            bean.tagLeft.toFloat(),
                            bean.tagRight.toFloat(),
                            bean.tagTop.toFloat(),
                            bean.tagBottom.toFloat()
                    ))
                }

            }
            map_layout.addSomeIcons(iconList)
        })
        /**
         * 设置地点点击事件
         */
        map_layout.setMyOnIconClickListener(object : MapLayout.OnIconClickListener {
            override fun onIconClick(v: View) {
                val bean = v.tag as IconBean
                map_layout.focusToPoint(bean.sx, bean.sy)
                viewModel.showPlaceDetails(bean.id)
            }

        })
        map_layout.setMyOnPlaceClickListener(object : MapLayout.OnPlaceClickListener {
            override fun onPlaceClick(v: View) {
                val bean = v.tag as IconBean
                //viewModel.showPlaceDetails(bean.id)
            }

        })
        /**
         * 初始化bottomSheet
         */
        bottomSheetBehavior = BottomSheetBehavior.from(map_bottom_sheet_content)
        map_bottom_sheet_content.invisible()
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            add(R.id.map_bottom_sheet_content, PlaceDetailBottomSheetFragment())
            commit()
        }

        /**
         * 初始化标签adapter（搜索框下方按钮）
         */
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        map_rv_symbol_places.layoutManager = linearLayoutManager
        val adapter = context?.let { SymbolRvAdapter(it, mutableListOf()) }
        map_rv_symbol_places.adapter = adapter
        /**
         * 注册监听
         */
        viewModel.buttonInfo.observe(
                viewLifecycleOwner,
                Observer { t ->
                    adapter?.setList(t.buttonInfo)
                    adapter?.notifyDataSetChanged()
                }
        )
        viewModel.placeDetails.observe(
                viewLifecycleOwner,
                Observer { t ->
                    map_bottom_sheet_content.visible()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

                }
        )


    }

}