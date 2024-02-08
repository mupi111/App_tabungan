package com.app.tabunganuts.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.app.tabunganuts.R
import com.app.tabunganuts.model.MoneyModel

class TabunganAdapter (val dataTabungan: List<MoneyModel>, var activity: Activity) : BaseAdapter() {

    private val inflater: LayoutInflater
            = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.tabungan_list, parent, false)

        val akunTextView = rowView.findViewById(R.id.txtNamaTabunganList) as TextView
        val jenisTransaksiTextView = rowView.findViewById(R.id.txtJenisTransaksiList) as TextView
        val timeTextView = rowView.findViewById(R.id.txtDateTimeList) as TextView
        val jumlahTextView = rowView.findViewById(R.id.txtJumlahList) as TextView
        val tabungan = getItem(position) as MoneyModel
        akunTextView.text =tabungan.nasabah
        jenisTransaksiTextView.text = tabungan.jenisTransaksi
        timeTextView.text ="Tanggal : "+tabungan.date+" "+"Pukul : "+tabungan.time
        jumlahTextView.text ="Rp. "+tabungan.jumlah.toString()
        notifyDataSetChanged()
        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataTabungan[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataTabungan.size
    }
}
