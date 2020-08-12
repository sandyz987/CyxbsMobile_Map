package com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.mredrock.cyxbs.common.utils.extensions.dp2px
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.databinding.MapFragmentPlaceDetailContainerBinding
import com.mredrock.cyxbs.discover.map.ui.activity.adapter.DetailAttributeRvAdapter
import com.mredrock.cyxbs.discover.map.ui.activity.adapter.DetailTagRvAdapter
import com.mredrock.cyxbs.discover.map.ui.activity.adpter.BannerRvAdapter
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel
import com.to.aboomy.pager2banner.IndicatorView
import com.to.aboomy.pager2banner.ScaleInTransformer
import kotlinx.android.synthetic.main.map_fragment_place_detail_container.*


class PlaceDetailBottomSheetFragment : Fragment() {
    private lateinit var viewModel: MapViewModel
    private lateinit var mBinding: MapFragmentPlaceDetailContainerBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.map_fragment_place_detail_container, container, false)
//        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)

        val indicator = IndicatorView(context)
                .setIndicatorColor(Color.DKGRAY)
                .setIndicatorSelectorColor(Color.WHITE)
                .setIndicatorRatio(1f) //ratio，默认值是1 ，也就是说默认是圆点，根据这个值，值越大，拉伸越长，就成了矩形，小于1，就变扁了呗
                .setIndicatorRadius(2f) // radius 点的大小
                .setIndicatorSelectedRatio(3f)
                .setIndicatorSelectedRadius(2f)
                .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_BIG_CIRCLE)
        val bannerAdapter = context?.let { BannerRvAdapter(it, mutableListOf()) }
        map_banner_detail_image.setIndicator(indicator).setPageMargin(context?.dp2px(20f)
                ?: 0, context?.dp2px(10f)
                ?: 0).addPageTransformer(ScaleInTransformer()).adapter = bannerAdapter

        val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                .setChildGravity(Gravity.TOP)
                .setScrollingEnabled(true)
                .setMaxViewsInRow(2)
                .setGravityResolver { Gravity.START }
                .setRowBreaker { position -> position == 6 || position == 11 || position == 2 }
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .withLastRow(true)
                .build()
        map_rv_detail_about_list.layoutManager = chipsLayoutManager
        val tagAdapter = context?.let { DetailTagRvAdapter(it, mutableListOf()) }
        map_rv_detail_about_list.adapter = tagAdapter

        val linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        map_rv_detail_place_attribute.layoutManager = linearLayoutManager
        val attributeAdapter = context?.let { DetailAttributeRvAdapter(it, mutableListOf()) }
        map_rv_detail_place_attribute.adapter = attributeAdapter
        /**
         * 对要显示的内容监听
         */
        viewModel.placeDetails.observe(
                viewLifecycleOwner,
                Observer { t ->
                    mBinding.placeDetails = t
                    if (bannerAdapter != null) {
                        bannerAdapter.setList(t.images)
                        bannerAdapter.notifyDataSetChanged()
                        map_banner_detail_image.adapter = bannerAdapter
                    }
                    if (tagAdapter != null) {
                        tagAdapter.setList(t.tags)
                        tagAdapter.notifyDataSetChanged()
                    }
                    if (attributeAdapter != null) {
                        attributeAdapter.setList(t.tags)
                        attributeAdapter.notifyDataSetChanged()
                    }
                }
        )
    }


}