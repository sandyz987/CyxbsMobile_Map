package com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mredrock.cyxbs.common.ui.BaseViewModelFragment
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.IconBean
import com.mredrock.cyxbs.discover.map.component.MapLayout
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel
import kotlinx.android.synthetic.main.map_fragment_map_view.*


class MapViewFragment : BaseViewModelFragment<MapViewModel>() {
    override val viewModelClass = MapViewModel::class.java
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    /**
     * 假设 Id = 0 太极运动场,Id = 1 风华运动场 。。。
     */
    companion object {
        const val SPORT_TAIJI = 0
        const val SPORT_FENGHUA = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.map_fragment_map_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /**
         * 初始化地图view
         */
        map_layout.addIcon(IconBean(0, 2945f, 6526f, 2430f, 3500f, 6210f, 6830f))
        map_layout.addIcon(IconBean(1, 2830f, 7488f, 2430f, 3250f, 7245f, 7717f))
        map_layout.setMyOnIconClickListener(object : MapLayout.OnIconClickListener {
            override fun onIconClick(v: View) {
                val bean = v.tag as IconBean
                map_layout.focusToPoint(bean.sx, bean.sy)
                /**
                 * 在此处添加标签响应事件
                 */
                when (bean.id) {
                    SPORT_TAIJI -> {
                        viewModel.toastEvent.value = R.string.map_tag_sport_taiji
                    }
                    SPORT_FENGHUA -> {
                        viewModel.toastEvent.value = R.string.map_tag_sport_fenghua
                    }
                }
            }

        })
        /**
         * 初始化bottomSheet
         */
        bottomSheetBehavior = BottomSheetBehavior.from(map_bottom_sheet_content)
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            add(R.id.map_bottom_sheet_content, PlaceDetailBottomSheetFragment())
            commit()
        }

    }

}