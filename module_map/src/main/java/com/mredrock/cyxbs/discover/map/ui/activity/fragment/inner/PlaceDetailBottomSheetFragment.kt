package com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mredrock.cyxbs.common.utils.extensions.dp2px
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.PlaceDetails
import com.mredrock.cyxbs.discover.map.databinding.MapFragmentPlaceDetailContainerBinding
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
        /**
         * 对要显示的内容监听
         */
        viewModel.placeDetails.observe(
                viewLifecycleOwner,
                Observer<PlaceDetails> { t ->
                    mBinding.placeDetails = t
                    if (bannerAdapter != null) {
                        bannerAdapter.setList(t.images)
                        bannerAdapter.notifyDataSetChanged()
                        map_banner_detail_image.adapter = bannerAdapter
                    }
                }
        )
    }


}