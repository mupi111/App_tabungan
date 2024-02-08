package com.app.tabunganuts.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.tabunganuts.adapter.NasabahAdapter
import com.app.tabunganuts.adapter.TabunganAdapter
import com.app.tabunganuts.helper.Constan
import com.app.tabunganuts.model.MoneyModel
import com.app.tabunganuts.model.NasabahModel
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MoneyApi (context: Context) {
    val  context: Context =context
    fun query(mode: String,body: MutableMap<String,String> ){
        var requestMethod:Int = Request.Method.GET
        when(mode){
            "show" -> requestMethod = Request.Method.GET
            "insert"-> requestMethod = Request.Method.POST
            "update" -> requestMethod = Request.Method.PUT
            "delete" -> requestMethod = Request.Method.DELETE
        }
        val request = object : StringRequest(
            requestMethod, Constan.URL+"/api/tabungans/"+if (mode == "update" || mode
                == "delete") body.get("id") else "",
            Response.Listener { response ->
                Log.d("tabungan",response.toString())
                if (JSONObject(response).get("success") as Boolean)
                    Toast.makeText(context,"Berhasil Merubah data", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context,"Gagal Merubah data", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Log.d("tabungan",error.toString())
                Toast.makeText(context,"Tidak dapat terhubung ke server CRUD", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                return body
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }
    fun query(mode: String, body: MutableMap<String,String>, listData:MutableList<MoneyModel>, adapter: TabunganAdapter){
        var requestMethod:Int = Request.Method.GET
        when(mode){
            "show" -> requestMethod = Request.Method.GET
            "insert"-> requestMethod = Request.Method.POST
            "update" -> requestMethod = Request.Method.PUT
            "delete" -> requestMethod = Request.Method.DELETE
        }
        val request = object : StringRequest(
            requestMethod, Constan.URL+"/api/tabungans/"+if (mode == "update" || mode
                == "delete") body.get("id") else "",
            Response.Listener { response ->
                listData.clear()
                val jsonArray = JSONObject(response).getJSONArray("data")
                for (x in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    listData.add(
                        MoneyModel(jsonObject.getString("id"),jsonObject.getString("nasabah"),jsonObject.getString("jenisTransaksi"),jsonObject.getString("date"),jsonObject.optString("time"), jsonObject.getInt("jumlah")))
                }
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->
                Log.d("nasabah",JSONArray(error).toString())
                Toast.makeText(context,"Tidak dapat terhubung ke server CRUD", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                return body
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }
}
