package com.mredrock.cyxbs.discover.map.model

import android.content.Context
import android.graphics.Bitmap
import com.google.gson.Gson
import com.mredrock.cyxbs.common.BaseApp
import com.mredrock.cyxbs.common.utils.extensions.defaultSharedPreferences
import com.mredrock.cyxbs.common.utils.extensions.editor
import com.mredrock.cyxbs.common.utils.extensions.sharedPreferences
import com.mredrock.cyxbs.discover.map.bean.MapInfo

/**
 *@author zhangzhe
 *@date 2020/8/17
 *@description 所有数据在此缓存
 */


object DataSet {
    private val defaultSp by lazy { BaseApp.context.sharedPreferences("map_cache") }
    private val gson = Gson()

    fun saveMap(bitmap: Bitmap) {

    }

    fun saveMapInfo(mapInfo: MapInfo) {
        val s = gson.toJson(mapInfo)
        defaultSp.editor {
            putString("MapInfoStore", s)
        }
    }

    fun getMapInfo(): MapInfo? {
        return gson.fromJson(defaultSp.getString("MapInfoStore", ""), MapInfo::class.java)
    }
}
