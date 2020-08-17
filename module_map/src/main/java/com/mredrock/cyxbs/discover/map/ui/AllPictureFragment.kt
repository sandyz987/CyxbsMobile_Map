package com.mredrock.cyxbs.discover.map.ui

import android.opengl.ETC1.getHeight
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
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
        map_tv_all_picture.alpha = 0f
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        map_rv_all_picture.layoutManager = staggeredGridLayoutManager
        allPictureAdapter = AllPictureRvAdapter(requireContext(), mutableListOf())
        map_rv_all_picture.adapter = allPictureAdapter

        map_iv_all_picture_back.setOnClickListener {
            viewModel.fragmentAllPictureIsShowing.value = false
        }

        map_rv_all_picture.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
                // 如果停止滑动
                if (state == SCROLL_STATE_IDLE) {
                    // 获取布局管理器
                    val layout = recyclerView.layoutManager as StaggeredGridLayoutManager
                    // 用来记录lastItem的position
                    // 由于瀑布流有多个列 所以此处用数组存储
                    val positions = IntArray(4)
                    // 获取lastItem的positions
                    /**
                     * 其他布局管理器可使用同样方式获取
                     */
                    layout.findLastVisibleItemPositions(positions)
                    for (i in positions.indices) {
                        /**
                         * 判断lastItem的底边到recyclerView顶部的距离
                         * 是否小于recyclerView的高度
                         * 如果小于或等于 说明滚动到了底部
                         */
                        if (layout.findViewByPosition(positions[i])!!.bottom <= recyclerView.height) {
                            /**
                             * 此处实现业务逻辑
                             */
                           map_tv_all_picture.animate().alpha(1f).duration = 500
                            return
                        } else{
                            map_tv_all_picture.animate().alpha(0f).duration = 500
                        }
                    }
                }
                super.onScrollStateChanged(recyclerView, state)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.placeDetails.value?.images != null) {

            allPictureAdapter.setList(viewModel.placeDetails.value?.images!!)
            allPictureAdapter.notifyDataSetChanged()
            map_rv_all_picture.adapter = allPictureAdapter
        }

    }
}