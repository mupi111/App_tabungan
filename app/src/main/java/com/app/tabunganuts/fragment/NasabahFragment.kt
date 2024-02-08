package com.app.tabunganuts.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.app.tabunganuts.MainActivity
import com.app.tabunganuts.R
import com.app.tabunganuts.activity.NasabahAddActivity
import com.app.tabunganuts.activity.NasabahAddActivity.Companion.UPDATE_AKUN
import com.app.tabunganuts.adapter.NasabahAdapter
import com.app.tabunganuts.model.NasabahModel
import com.app.tabunganuts.network.NasabahApi
import kotlinx.android.synthetic.main.fragment_money.*

class NasabahFragment : Fragment() {
    lateinit var adapterNasabah: NasabahAdapter
    lateinit var  nasabahApi: NasabahApi
    var listNasabah = mutableListOf<NasabahModel>()
    var body = HashMap<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        nasabahApi = NasabahApi(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_money, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        btnAdd.setOnClickListener {
            val intent = Intent(activity, NasabahAddActivity::class.java)
            startActivity(intent)
        }

        lvData.setOnItemClickListener { adapterView, view, i, l ->
            var menuPopup = PopupMenu(activity,view)
            menuPopup.menuInflater.inflate(R.menu.menu_popup,menuPopup.menu)
            menuPopup.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.menuUpdate -> {
                        var intent = Intent(activity, NasabahAddActivity::class.java)
                        intent.putExtra(UPDATE_AKUN,listNasabah[i])
                        activity?.startActivity(intent)
                        true
                    }
                    R.id.menuDelete -> {
                        Toast.makeText(activity,"Hapus "+listNasabah[i].nama,Toast.LENGTH_LONG)
                        body.put("id", listNasabah[i].id)
                        nasabahApi.query("delete",body)
                        showData()
                        true
                    }

                }
                false
            }
            menuPopup.show()
        }

    }

    override fun onResume() {
        showData()
        super.onResume()
    }


    fun showData(){
        adapterNasabah = NasabahAdapter(listNasabah, activity as MainActivity)
        lvData.adapter = adapterNasabah
        body.put("nama","")
        nasabahApi.query("show", body,listNasabah, adapterNasabah)

    }
}
