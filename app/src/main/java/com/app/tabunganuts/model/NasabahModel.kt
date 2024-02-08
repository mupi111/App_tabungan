package com.app.tabunganuts.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NasabahModel(
    var id:String,
    var nama:String,
    var alamat:String,
    var nomorhp:String,
    var imgnasabah:String,
    var saldo:Int,
): Parcelable
