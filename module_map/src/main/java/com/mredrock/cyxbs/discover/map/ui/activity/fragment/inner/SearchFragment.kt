package com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mredrock.cyxbs.common.ui.BaseViewModelFragment
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.PlaceDetails
import com.mredrock.cyxbs.discover.map.model.TestData
import com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner.search.SearchHistoryFragment
import com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner.search.SearchResultFragment
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel
import kotlinx.android.synthetic.main.map_fragment_search.*

class SearchFragment : Fragment() {
    private lateinit var viewModel: MapViewModel
    private val manager: FragmentManager?
        get() = childFragmentManager

    private var searchHistoryFragment = SearchHistoryFragment()
    private var searchResultFragment = SearchResultFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)

        viewModel.searchText.observe(
                viewLifecycleOwner,
                Observer {
                    if (it.isEmpty()) {
                        openSearchHistoryFragment()
                    } else {
                        openSearchResultFragment()
                    }
                }
        )
        Log.d("sandyzhang", "ob================")


    }

    override fun onSaveInstanceState(outState: Bundle) {
        val transaction = manager?.beginTransaction()
        searchResultFragment.removeObserver()
        transaction?.remove(searchHistoryFragment)?.remove(searchResultFragment)?.commit()
        super.onSaveInstanceState(outState)
    }


    private fun openSearchHistoryFragment() {
        val transaction = manager?.beginTransaction()
        searchHistoryFragment = SearchHistoryFragment()
        if (searchResultFragment.isAdded) {
            transaction?.hide(searchResultFragment)
        }
        if (!searchHistoryFragment.isAdded) {
            transaction?.add(R.id.map_fl_search_fragment, searchHistoryFragment)
        }
        Log.d("sandyzhang", "show================" + (manager == null))
        transaction?.show(searchHistoryFragment)?.commit()
    }


    private fun openSearchResultFragment() {
        val transaction = manager?.beginTransaction()
        searchResultFragment = SearchResultFragment()
        if (searchHistoryFragment.isAdded) {
            transaction?.hide(searchHistoryFragment)
        }
        if (!searchResultFragment.isAdded) {
            transaction?.add(R.id.map_fl_search_fragment, searchResultFragment)
        }
        transaction?.show(searchResultFragment)?.commit()
    }

}