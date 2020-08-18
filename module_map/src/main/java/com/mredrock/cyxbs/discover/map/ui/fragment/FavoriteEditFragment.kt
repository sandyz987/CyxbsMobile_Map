package com.mredrock.cyxbs.discover.map.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.databinding.MapFragmentFavoriteEditBinding
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel
import com.mredrock.cyxbs.discover.map.widget.MapDialog
import com.mredrock.cyxbs.discover.map.widget.OnSelectListener
import com.mredrock.cyxbs.discover.map.widget.ProgressDialog
import kotlinx.android.synthetic.main.map_fragment_favorite_edit.*

class FavoriteEditFragment : Fragment() {
    private lateinit var viewModel: MapViewModel
    private lateinit var mBinding: MapFragmentFavoriteEditBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.map_fragment_favorite_edit, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
        map_tv_favorite_cancel.setOnClickListener {
            viewModel.fragmentFavoriteEditIsShowing.value = false
        }



        map_tv_favorite_cancel_favorite.setOnClickListener {
            MapDialog.show(requireContext(), "取消收藏", resources.getString(R.string.map_favorite_delete), object : OnSelectListener {
                override fun onDeny() {
                }

                override fun onPositive() {
                    viewModel.fragmentFavoriteEditIsShowing.value = false
                    viewModel.deleteCollect(viewModel.showingPlaceId)
                    ProgressDialog.show(requireActivity(), "请稍后", "正在取消收藏", false)

                }
            })
        }
        map_tv_favorite_accept.setOnClickListener {
            if (map_et_favorite_nickname.length() != 0) {
                viewModel.fragmentFavoriteEditIsShowing.value = false
                ProgressDialog.show(requireActivity(), "请稍后", "正在添加收藏", false)

                viewModel.addCollect(map_et_favorite_nickname.text.toString(), viewModel.showingPlaceId)
            } else {
                viewModel.toastEvent.value = R.string.map_favorite_edit_length_not_enough
            }
        }

    }

    override fun onResume() {
        super.onResume()
        map_et_favorite_nickname.maxStringLength = 12
        if (viewModel.collectList.value != null) {
            var s = viewModel.placeDetails.value?.placeName
            for (t in viewModel.collectList.value!!) {
                if (t.placeId == viewModel.showingPlaceId) {
                    s = t.placeNickname
                    break
                }
            }
            map_et_favorite_nickname.setText(s)
        } else {
            map_et_favorite_nickname.setText(viewModel.placeDetails.value?.placeName)
        }

        map_et_favorite_nickname.notifyStringChange()
        map_tv_favorite_place_name.text = viewModel.placeDetails.value?.placeName
    }

}