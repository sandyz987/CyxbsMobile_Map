package com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mredrock.cyxbs.common.utils.extensions.dp2px
import com.mredrock.cyxbs.common.utils.extensions.invisible
import com.mredrock.cyxbs.common.utils.extensions.visible
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.FavoritePlace
import com.mredrock.cyxbs.discover.map.bean.IconBean
import com.mredrock.cyxbs.discover.map.bean.PlaceItem
import com.mredrock.cyxbs.discover.map.component.MapLayout
import com.mredrock.cyxbs.discover.map.ui.activity.adapter.FavoriteListAdapter
import com.mredrock.cyxbs.discover.map.ui.activity.adapter.SymbolRvAdapter
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel
import kotlinx.android.synthetic.main.map_fragment_map_view.*
import org.jetbrains.anko.sdk27.coroutines.onFocusChange
import org.jetbrains.anko.sdk27.coroutines.onTouch


class MapViewFragment : Fragment() {
    private lateinit var viewModel: MapViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private val placeData = mutableListOf<PlaceItem>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment_map_view, container, false)
    }

    @SuppressLint("ResourceAsColor")
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
        /**
         * 监听点击到建筑区域的点击事件
         */
        map_layout.setMyOnPlaceClickListener(object : MapLayout.OnPlaceClickListener {
            override fun onPlaceClick(v: View) {
                val bean = v.tag as IconBean
                //viewModel.showPlaceDetails(bean.id)
            }

        })

        /**
         * 监听点击到非建筑区域的点击事件
         */
        map_layout.setMyOnNoPlaceClickListener(object : MapLayout.OnNoPlaceClickListener {
            override fun onNoPlaceClick() {

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
        val symbolRvAdapter = context?.let { SymbolRvAdapter(it, mutableListOf()) }
        map_rv_symbol_places.adapter = symbolRvAdapter
        /**
         * 初始化我的收藏列表adapter
         * （弹窗）
         */

        val popView = View.inflate(requireContext(), R.layout.map_pop_window_favorite_list, null)
        val popupWindow = PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow.isOutsideTouchable = true

        val mapFavoriteRecyclerView = popView.findViewById<RecyclerView>(R.id.map_rv_favorite_list)
        mapFavoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val favoriteListAdapter = FavoriteListAdapter(requireContext(), mutableListOf())
        mapFavoriteRecyclerView.adapter = favoriteListAdapter
        //设置点击事件
        map_ll_map_view_my_favorite.setOnClickListener {
            if (!popupWindow.isShowing) {
                popupWindow.showAsDropDown(map_ll_map_view_my_favorite, map_ll_map_view_my_favorite.width - (context?.dp2px(140f)
                        ?: 30), context?.dp2px(15f) ?: 30)
                popupWindow.update()
            }
        }


        /**
         * 注册监听
         */
        viewModel.buttonInfo.observe(
                viewLifecycleOwner,
                Observer {
                    symbolRvAdapter?.setList(it.buttonInfo)
                }
        )


        viewModel.placeDetails.observe(
                viewLifecycleOwner,
                Observer {
                    //展开底部栏
                    map_bottom_sheet_content.visible()
                    //下面这两句，因为低版本依赖的bottomSheetBehavior，当内部view高度发生变化时，不会及时修正高度，故手动测量
                    //换成高版本依赖可以删除
                    map_bottom_sheet_content.requestLayout()
                    map_bottom_sheet_content.invalidate()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    map_bottom_sheet_content.onTouchEvent(MotionEvent.obtain(0L, 0L, MotionEvent.ACTION_DOWN, 50f, 50f, 1))
                }
        )
        viewModel.favoriteList.observe(
                viewLifecycleOwner,
                Observer {
                    //更新favoriteList数据
                    favoriteListAdapter.setList(it)
                }
        )


    }

}