package com.app.tabunganuts.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.app.tabunganuts.R
import com.app.tabunganuts.model.NasabahModel
import com.squareup.picasso.Picasso

class NasabahAdapter(val dataNasabah: List<NasabahModel>, var activity: Activity) : BaseAdapter() {
    private val inflater: LayoutInflater
            = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.akun_list, parent, false)

        val namaTextView = rowView.findViewById(R.id.txtNamaAkunList) as TextView
        val alamatTextView = rowView.findViewById(R.id.txtAlamatList) as TextView
        val nomorHpTextView = rowView.findViewById(R.id.txtHPList) as TextView
        val imgNasabahImageView = rowView.findViewById(R.id.imgAkunList) as ImageView
        val saldoTextView = rowView.findViewById(R.id.txtSaldo) as TextView
        val akun = getItem(position) as NasabahModel
        namaTextView.text =akun.nama
        alamatTextView.text =akun.alamat
        nomorHpTextView.text =akun.nomorhp
        saldoTextView.text ="Rp. "+akun.saldo.toString()
        Picasso.get().load(akun.imgnasabah).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imgNasabahImageView)

        notifyDataSetChanged()
        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataNasabah[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataNasabah.size
    }
}
