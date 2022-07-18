package com.hadirahimi.locationFinder.Model


import com.google.gson.annotations.SerializedName

data class ResponseAddress(
    @SerializedName("addresses")
    val addresses: List<Addresse?>?,
    @SerializedName("formatted_address")
    val formattedAddress: String?,
    @SerializedName("municipality_zone")
    val municipalityZone: String?,
    @SerializedName("neighbourhood")
    val neighbourhood: String?,
    @SerializedName("route_name")
    val routeName: String?,
    @SerializedName("route_type")
    val routeType: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("status")
    val status: String?
) {
    data class Addresse(
        @SerializedName("components")
        val components: List<Component?>?,
        @SerializedName("formatted")
        val formatted: String?
    ) {
        data class Component(
            @SerializedName("name")
            val name: String?,
            @SerializedName("type")
            val type: String?
        )
    }
}