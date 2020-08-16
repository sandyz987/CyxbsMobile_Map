package com.mredrock.cyxbs.discover.map.ui.inner

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
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
import com.mredrock.cyxbs.discover.map.bean.IconBean
import com.mredrock.cyxbs.discover.map.bean.PlaceItem
import com.mredrock.cyxbs.discover.map.component.MapLayout
import com.mredrock.cyxbs.discover.map.ui.activity.VRActivity
import com.mredrock.cyxbs.discover.map.ui.adapter.FavoriteListAdapter
import com.mredrock.cyxbs.discover.map.ui.adapter.SymbolRvAdapter
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel
import kotlinx.android.synthetic.main.map_fragment_map_view.*


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
         *
         */
        map_layout.setMyOnNoPlaceClickListener(object : MapLayout.OnNoPlaceClickListener {
            override fun onNoPlaceClick() {

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
                //通知隐藏底部栏
                viewModel.bottomSheetIsShowing.value = false
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

        map_iv_compass.setOnClickListener {
            map_layout.focusToPoint("2")
        }
        /**
         * 注册监听
         */
        viewModel.buttonInfo.observe(
                viewLifecycleOwner,
                Observer {
                    if (it.buttonInfo != null) {
                        symbolRvAdapter?.setList(it.buttonInfo!!)
                    }

                }
        )


        viewModel.bottomSheetIsShowing.observe(
                viewLifecycleOwner,
                Observer {
                    if (it) {
                        //展开底部栏
                        map_bottom_sheet_content.visible()
                        //下面这两句，因为低版本依赖的bottomSheetBehavior，当内部view高度发生变化时，不会及时修正高度，故手动测量
                        //换成高版本依赖可以删除
                        map_bottom_sheet_content.requestLayout()
                        map_bottom_sheet_content.invalidate()
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    } else {
                        //半隐藏底部栏
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
        )
        viewModel.favoriteList.observe(
                viewLifecycleOwner,
                Observer {
                    //更新favoriteList数据
                    favoriteListAdapter.setList(it)
                }
        )

        viewModel.showSomeIconsId.observe(viewLifecycleOwner, Observer {
            map_layout.closeAllIcon()
            map_layout.showSomeIcons(it)
            map_layout.focusToPoint(it[0])
        })


        map_iv_vr.setOnClickListener {
            val xc: Int = (map_root_map_view.left + map_root_map_view.right) / 2
            val yc: Int = (map_root_map_view.top + map_root_map_view.bottom) / 2
            val animator = ViewAnimationUtils.createCircularReveal(map_root_map_view, xc, yc, map_root_map_view.height.toFloat() + 100f, 0f)
            animator.interpolator = DecelerateInterpolator()
            animator.duration = 1000
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    val intent = Intent(context, VRActivity::class.java)
                    startActivity(intent)
                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationStart(p0: Animator?) {
                    viewModel.isAnimation.value = true
                    map_root_map_view.animate().alpha(0f).duration = 1000
                }

            })
            animator.start()


        }
    }

    override fun onResume() {
        map_root_map_view.animate().alpha(1f).duration = 1000
        viewModel.isAnimation.value = false
        super.onResume()
    }
}