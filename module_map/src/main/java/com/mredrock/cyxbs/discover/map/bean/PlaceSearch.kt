package com.mredrock.cyxbs.discover.map.bean

import com.google.gson.annotations.SerializedName

data class PlaceSearch(
        @SerializedName("place_id")
        var placeId: String
)
