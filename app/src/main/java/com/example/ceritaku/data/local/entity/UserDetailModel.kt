package com.example.ceritaku.data.local.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserDetailModel(
    var name : String? = "",
    var token : String? = "",
    var onBoard : Boolean = false,
    var theme : Boolean = false
): Parcelable
