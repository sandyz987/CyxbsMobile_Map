package com.mredrock.cyxbs.discover.map.model

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mredrock.cyxbs.common.BaseApp
import com.mredrock.cyxbs.common.bean.RedrockApiWrapper
import com.mredrock.cyxbs.common.utils.extensions.editor
import com.mredrock.cyxbs.common.utils.extensions.sharedPreferences
import com.mredrock.cyxbs.discover.map.bean.ButtonInfo
import com.mredrock.cyxbs.discover.map.bean.FavoritePlace
import com.mredrock.cyxbs.discover.map.bean.MapInfo
import com.mredrock.cyxbs.discover.map.bean.PlaceItem

/**
 *@author zhangzhe
 *@date 2020/8/17
 *@description 所有数据在此缓存
 */


object DataSet {
    private val sharedPreferences by lazy { BaseApp.context.sharedPreferences("map_cache") }
    private val gson = Gson()

    fun saveMapInfo(mapInfo: MapInfo) {
        val s = gson.toJson(mapInfo)
        sharedPreferences.editor {
            putString("MapInfoStore", s)
        }
    }

    fun getMapInfo(): MapInfo? {
        val s1 = sharedPreferences.getString("MapInfoStore", "")
        return if (s1 == "") {
            null
        } else {
            Log.e("sandyzhang", s1)
            gson.fromJson(s1, MapInfo::class.java)
        }
    }

    fun saveButtonInfo(buttonInfo: ButtonInfo) {
        val s = gson.toJson(buttonInfo)
        sharedPreferences.editor {
            putString("ButtonInfoStore", s)
        }
    }

    fun getButtonInfo(): ButtonInfo? {
        val s = sharedPreferences.getString("ButtonInfoStore", "")
        return if (s != "") {
            gson.fromJson(s, ButtonInfo::class.java)
        } else {
            null
        }
    }

    fun addCollect(id: String) {
        val s = sharedPreferences.getString("FavoritePlaceStore", "")
        var list: MutableList<FavoritePlace>? =
                if (s != "") {
                    gson.fromJson(s, object : TypeToken<MutableList<FavoritePlace>>() {}.type)
                } else {
                    null
                }


        if (list == null) {
            list = mutableListOf()
        }
        var isExist = false
        for (t: FavoritePlace in list) {
            if (t.placeId == id) {
                isExist = true
            }
        }
        //如果没收藏该id
        if (!isExist) {
            var placeName = "收藏" + list.size.toString()
            val mapInfo: MapInfo? = getMapInfo()
            if (mapInfo != null) {
                for (t: PlaceItem in mapInfo.placeList) {
                    if (t.placeId == id) {
                        placeName = t.placeName
                    }
                }
            }
            list.add(FavoritePlace(placeName, id))
        }
        sharedPreferences.editor {
            putString("FavoritePlaceStore", gson.toJson(list))
        }
    }

    fun addCollect(favoritePlace: FavoritePlace) {
        val s = sharedPreferences.getString("FavoritePlaceStore", "")
        var list: MutableList<FavoritePlace>? =
                if (s != "") {
                    gson.fromJson(s, object : TypeToken<MutableList<FavoritePlace>>() {}.type)
                } else {
                    null
                }
        if (list != null) {
            var flag = true
            for (t: FavoritePlace in list) {
                if (t.placeId == favoritePlace.placeId) {
                    t.placeNickname = favoritePlace.placeNickname
                    flag = false
                    break
                }
            }
            if (flag) {
                list.add(favoritePlace)
            }
        } else {
            list = mutableListOf(favoritePlace)
        }
        sharedPreferences.editor {
            putString("FavoritePlaceStore", gson.toJson(list))
        }
    }

    fun deleteCollect(id: String) {
        val s = sharedPreferences.getString("FavoritePlaceStore", "")
        var list: MutableList<FavoritePlace>? =
                if (s != "") {
                    gson.fromJson(s, object : TypeToken<MutableList<FavoritePlace>>() {}.type)
                } else {
                    null
                }
        if (list != null) {
            for (f: FavoritePlace in list) {
                if (f.placeId == id) {
                    list.remove(f)
                    break
                }
            }
        } else {
            list = mutableListOf()
        }
        sharedPreferences.editor {
            putString("FavoritePlaceStore", gson.toJson(list))
        }
    }

    fun getCollect(): MutableList<FavoritePlace>? {
        val s = sharedPreferences.getString("FavoritePlaceStore", "")
        return if (s != "") {
            gson.fromJson(s, object : TypeToken<MutableList<FavoritePlace>>() {}.type)
        } else {
            null
        }
    }


}