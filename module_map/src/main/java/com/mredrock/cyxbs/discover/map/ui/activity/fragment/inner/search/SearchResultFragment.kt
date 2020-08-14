package com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.PlaceItem
import com.mredrock.cyxbs.discover.map.ui.activity.adapter.SearchResultAdapter
import com.mredrock.cyxbs.discover.map.util.ThreadPool
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.map_fragment_search_result.*
import okhttp3.internal.platform.Platform
import org.jetbrains.anko.support.v4.runOnUiThread
import java.util.regex.Pattern

class SearchResultFragment : Fragment() {
    private lateinit var viewModel: MapViewModel
    private lateinit var observer: Observer<String>
    private var isSearching = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)


        val searchResultAdapter = SearchResultAdapter(requireContext(), viewModel)
        map_rv_search_result.adapter = searchResultAdapter
        map_rv_search_result.layoutManager = LinearLayoutManager(requireContext())
        observer = Observer { t ->
            if (t == "" || isSearching) {
                return@Observer
            }
            isSearching = true
            ThreadPool.getInstance().execute {
                //输入每次变化都执行下列搜索
                val searchResultArrayList = ArrayList<PlaceItem>()
                val pattern = Pattern.compile(t)
                for (placeItem: PlaceItem in viewModel.mapInfo.value?.placeList ?: listOf()) {
                    val matcher = pattern.matcher(placeItem.placeName)
                    if (matcher.find()) {
                        searchResultArrayList.add(placeItem)
                    }
                }
                Log.e("sandyzhang", searchResultArrayList.size.toString() + if (searchResultArrayList.size >= 1) searchResultArrayList[0].placeName else "")
                //以上是搜索到的结果
                //如果现存列表没有搜索到的结果，则添加
                for (placeItemResult: PlaceItem in searchResultArrayList) {
                    var flag = false
                    for (placeItemOrigin: PlaceItem in viewModel.searchResult) {
                        if (placeItemOrigin === placeItemResult) {
                            flag = true
                        }
                    }
                    if (!flag) {
                        Thread.sleep(50)
                        runOnUiThread {
                            viewModel.searchResult.add(placeItemResult)
                        }
                    }
                }
                //如果现存列表有多余的结果，则减少
                for (placeItemOrigin: PlaceItem in viewModel.searchResult.reversed().toList()) {
                    var flag = false
                    for (placeItemResult: PlaceItem in searchResultArrayList) {
                        if (placeItemOrigin === placeItemResult) {
                            flag = true
                        }
                    }
                    if (!flag) {
                        Thread.sleep(50)
                        runOnUiThread {
                            viewModel.searchResult.remove(placeItemOrigin)
                        }
                    }
                }
                isSearching = false

            }
        }
        viewModel.searchText.removeObserver(observer)
        viewModel.searchText.observe(
                viewLifecycleOwner,
                observer
        )


    }

    fun removeObserver() {
        viewModel.searchText.removeObserver(observer)
    }

}