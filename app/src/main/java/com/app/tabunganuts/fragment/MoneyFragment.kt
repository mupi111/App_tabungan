package com.app.tabunganuts.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.app.tabunganuts.MainActivity
import com.app.tabunganuts.R
import com.app.tabunganuts.activity.NasabahAddActivity
import com.app.tabunganuts.activity.MoneyAddActivity
import com.app.tabunganuts.activity.MoneyAddActivity.Companion.UPDATE_TABUNGAN
import com.app.tabunganuts.adapter.TabunganAdapter
import com.app.tabunganuts.model.MoneyModel
import com.app.tabunganuts.network.MoneyApi
import kotlinx.android.synthetic.main.fragment_money.*
import java.util.HashMap

class MoneyFragment : Fragment() {
    lateinit var adapterTabungan: TabunganAdapter
    lateinit var moneyApi: MoneyApi
    var listMoney = mutableListOf<MoneyModel>()
    var body = HashMap<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        moneyApi = MoneyApi(requireContext())
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
        adapterTabungan = TabunganAdapter(listMoney, activity as MainActivity)
        lvData.adapter = adapterTabungan
        btnAdd.setOnClickListener {
            val intent = Intent(activity, MoneyAddActivity::class.java)
//            intent.putExtra("send_add", SEND_ADD_SHOES)
            startActivity(intent)
        }
        lvData.setOnItemClickListener { adapterView, view, i, l ->
            var menuPopup = PopupMenu(activity, view)
            menuPopup.menuInflater.inflate(R.menu.menu_popup, menuPopup.menu)
            menuPopup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuUpdate -> {
                        var intent = Intent(activity, MoneyAddActivity::class.java)
                        intent.putExtra(UPDATE_TABUNGAN, listMoney[i])
                        activity?.startActivity(intent)
                        true
                    }
                    R.id.menuDelete -> {
                        body.put("id", listMoney[i].id)
                        moneyApi.query("delete",body)
                        showData()
                        adapterTabungan.notifyDataSetChanged()
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

        body.put("nama","")
        moneyApi.query("show",body ,listMoney,adapterTabungan)
    }
}
