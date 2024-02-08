package com.app.tabunganuts.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.tabunganuts.MainActivity.Companion.listNasabahData
import com.app.tabunganuts.adapter.NasabahAdapter
import com.app.tabunganuts.helper.Constan
import com.app.tabunganuts.model.NasabahModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class NasabahApi (context: Context) {
    val context: Context = context
    fun query(mode: String,body: MutableMap<String,String> ){
        var requestMethod:Int = Request.Method.GET
        when(mode){
            "show" -> requestMethod = Request.Method.GET
            "insert"-> requestMethod = Request.Method.POST
            "update" -> requestMethod = Request.Method.PUT
            "delete" -> requestMethod = Request.Method.DELETE
        }
        val request = object : StringRequest(
            requestMethod, Constan.URL+"/api/nasabahs/"+if (mode == "update" || mode
                == "delete") body.get("id") else "",
            Response.Listener { response ->
                Log.d("nasabah",response.toString())
                if (JSONObject(response).get("success") as Boolean)
                    Toast.makeText(context,"Berhasil Merubah data", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context,"Gagal Merubah data", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(context,"Tidak dapat terhubung ke server CRUD", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                return body
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }
    fun query(mode: String,body: MutableMap<String,String> ,listNasabah:MutableList<NasabahModel>, adapter: NasabahAdapter){
        var requestMethod:Int = Request.Method.GET
        when(mode){
            "show" -> requestMethod = Request.Method.GET
            "insert"-> requestMethod = Request.Method.POST
            "update" -> requestMethod = Request.Method.PUT
            "delete" -> requestMethod = Request.Method.DELETE
        }
        val request = object : StringRequest(
            requestMethod, Constan.URL+"/api/nasabahs/"+if (mode == "update" || mode
                == "delete") body.get("id") else "",
            Response.Listener { response ->
                listNasabah.clear()
                val jsonArray = JSONObject(response).getJSONArray("data")
                for (x in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    listNasabah.add(
                        NasabahModel(jsonObject.getString("id"),jsonObject.getString("nama"),jsonObject.getString("alamat"),jsonObject.getString("nomorhp"),Constan.URL+"/storage/"+jsonObject.optString("imgnasabah"), jsonObject.getInt("saldo")))
                }
                listNasabahData.clear()
                listNasabahData= listNasabah
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
