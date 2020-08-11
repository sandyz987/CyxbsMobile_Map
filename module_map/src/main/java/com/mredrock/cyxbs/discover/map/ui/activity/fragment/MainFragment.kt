package com.mredrock.cyxbs.discover.map.ui.activity.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.mredrock.cyxbs.common.ui.BaseViewModelFragment
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner.MapViewFragment
import com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner.SearchFragment
import com.mredrock.cyxbs.discover.map.utils.KeyboardController
import com.mredrock.cyxbs.discover.map.viewmodels.MapViewModel
import kotlinx.android.synthetic.main.map_fragment_main.*

//该MainFragment使用FragmentTransaction管理两个Fragment

class MainFragment : BaseViewModelFragment<MapViewModel>() {
    override val viewModelClass = MapViewModel::class.java

    private val manager: FragmentManager?
        get() = activity?.supportFragmentManager
    private val mapViewFragment = MapViewFragment()
    private val searchFragment = SearchFragment()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment_main, container, false)
    }



    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapViewFragment()
        map_iv_back.setOnClickListener {
            //Toast.makeText(requireContext(),manager?.backStackEntryCount.toString(),Toast.LENGTH_SHORT).show()
            if(manager?.backStackEntryCount?: 0 == 0){
                //此处填写退出MapActivity的逻辑
                activity?.finish()
            } else {
                closeSearchFragment()
                map_et_search.clearFocus()
            }
            KeyboardController.hideInputKeyboard(requireContext(), it)
        }
        //当搜索框被点击，打开搜索Fragment
        map_et_search.setOnClickListener {
            openSearchFragment()
        }
        //上面这个事件在editText没有焦点时收不到事件，于是加一个
        map_et_search.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                openSearchFragment()
            }
        }


    }

    private fun initMapViewFragment(){
        val transaction = manager?.beginTransaction()
        transaction?.add(R.id.map_ll_map_fragment, mapViewFragment)?.commit()
    }

    private fun closeSearchFragment(){
        val transaction = manager?.beginTransaction()?.setCustomAnimations(R.animator.map_slide_in_left,R.animator.map_slide_out_right,0,0)
        transaction?.hide(searchFragment)
        if(!mapViewFragment.isAdded){
            transaction?.add(R.id.map_ll_map_fragment, mapViewFragment)?.commit()
        }else{
            transaction?.show(mapViewFragment)?.commit()
        }
        manager?.popBackStack()
    }


    private fun openSearchFragment(){
        if(manager?.backStackEntryCount?: 0 != 0){
            //确保不重复打开搜索框
            return
        }
        val transaction = manager?.beginTransaction()?.setCustomAnimations(R.animator.map_slide_in_left,R.animator.map_slide_out_right,0,0)
        transaction?.hide(mapViewFragment)
        if(!searchFragment.isAdded){
            transaction?.add(R.id.map_ll_map_fragment, searchFragment)?.show(searchFragment)?.commit()
        }else{
            transaction?.show(searchFragment)?.commit()
        }
        transaction?.addToBackStack("search")
    }




}