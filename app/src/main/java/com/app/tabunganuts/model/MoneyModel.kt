package com.app.tabunganuts.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Date

@Parcelize
data class MoneyModel(
    var id:String,
    var nasabah:String,
    var jenisTransaksi:String,
    var date:String,
    var time:String,
    var jumlah:Int,
): Parcelable
