package com.app.tabunganuts.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.app.tabunganuts.MainActivity
import com.app.tabunganuts.R
import com.app.tabunganuts.model.NasabahModel
import com.app.tabunganuts.network.NasabahApi

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_money_add.*
import kotlinx.android.synthetic.main.akun_list.*
import mumayank.com.airlocationlibrary.AirLocation
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class NasabahAddActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object{
        var UPDATE_AKUN:String = "Update Nasabah"
    }
    var lat : Double = 0.0; var lng : Double = 0.0;
    var airLoc : AirLocation? = null
    var gMap : GoogleMap? = null
    lateinit var mapFragment : SupportMapFragment
    private val MAPBOX_TOKEN = "pk.eyJ1IjoiaGlsbWl5dXN1Zjc4IiwiYSI6ImNsNDhlMm8yNDA2ZHIzZG8yc2xmbXMxdzYifQ.RLnKKlkytbMLIDkLRa4RTg"
    //    pk.eyJ1IjoiaGlsbWl5dXN1Zjc4IiwiYSI6ImNsNDg4Mm95ZjBodWUzZG5jYjl2dXc3eHEifQ.OqUO5VVve0HXmgGu4ThZHA
    var URL = ""
    var imstr = ""
    var namaFile = ""
    var fileUri = Uri.parse("")
    lateinit var photoHelper : PhotoHelper
    lateinit var mediaHelper: MediaHelper
     var selectMedia =true
    lateinit var  nasabahApi: NasabahApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money_add)
        layout_nasabah.visibility = View.VISIBLE
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapsFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e:Exception) {
            e.printStackTrace()
        }

        photoHelper = PhotoHelper()
        mediaHelper = MediaHelper(this)
        nasabahApi = NasabahApi(this)

        radioAlamatNasabah.setOnCheckedChangeListener { radioGroup, i ->
            var radioButton = radioGroup.findViewById<RadioButton>(radioGroup.getCheckedRadioButtonId())
            if (radioButton.text == "Alamat"){
                inpAlamatAkun.visibility = View.VISIBLE
                layoutMaps.visibility = View.GONE
                inpAlamatAkun.editText?.setText("")
            }else{
                inpAlamatAkun.visibility = View.VISIBLE
                layoutMaps.visibility = View.VISIBLE
                airLoc = AirLocation(this,true,true,
                    object : AirLocation.Callbacks{
                        override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                            Toast.makeText(this@NasabahAddActivity, "Gagal mendapatkan posisi saat ini",
                                Toast.LENGTH_SHORT).show()
                        }

                        override fun onSuccess(location: Location) {
                            val ll = LatLng(location.latitude,location.longitude)
                            URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/${location.longitude},${location.latitude}.json?types=place%2Cpostcode%2Caddress&access_token=" +
                                    "$MAPBOX_TOKEN&limit=1"
                            getAddressLocation(URL)
                        }
                    })
            }
        }
        radioImageNasabah.setOnCheckedChangeListener { radioGroup, i ->
            var radioButton = radioGroup.findViewById<RadioButton>(radioGroup.getCheckedRadioButtonId())
            if (radioButton.text.toString() == "Image Camera" ){
                selectMedia = false
                kameraInput()
            }else{
                imageViewAkun.visibility = View.VISIBLE
                selectMedia =true
                val intent = Intent()
                intent.setType("image/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(intent, mediaHelper.getRcGallery())
            }
        }

        var intentGet: NasabahModel? = intent?.getParcelableExtra<NasabahModel>(UPDATE_AKUN)
        if (intentGet==null){
            getSupportActionBar()!!.setTitle("Add Akun");

            btnAddAkunTabungan.setOnClickListener {
                nasabahApi.query("insert", inputAKun()).apply {
                finish()
                }
            }
        }else{
            getSupportActionBar()!!.setTitle("Update Akun");
            inpNamaAkun.editText?.setText(intentGet.nama)
            inpAlamatAkun.editText?.setText(intentGet.alamat)
            inpAlamatAkun.visibility = View.VISIBLE
            inpHPAkun.editText?.setText(intentGet.nomorhp)
            Picasso.get().load(intentGet.imgnasabah).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageViewAkun)
            imageViewAkun.visibility = View.VISIBLE
            btnAddAkunTabungan.setText("Update")
            btnAddAkunTabungan.setOnClickListener {
                nasabahApi.query("update", inputAKun(intentGet.id)).apply {
                    finish()
                }
            }
        }
    }
    fun kameraInput() = runWithPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA ) {
        fileUri = photoHelper.getOutputMediaFileUri()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, photoHelper.getRcCamera())
    }
    fun getAddressLocation(url : String){
        val request = JsonObjectRequest(
            Request.Method.GET,url,null,
            {

                val features = it.getJSONArray("features").getJSONObject(0)
//                Log.d("alamat-map",it.toString())
                val place_name = features.getString("place_name")
                inpAlamatAkun.editText?.setText(place_name.toString())

            }, {
                Toast.makeText(this,"can't get destination location", Toast.LENGTH_SHORT).show()
            })
        val q = Volley.newRequestQueue(this)
        q.add(request)
    }


    fun inputAKun(id:String=""):HashMap<String,String> {
        var body = HashMap<String,String>()
        body.put("nama", inpNamaAkun.editText?.text.toString())
        body.put("id",id)
        body.put("alamat",inpAlamatAkun.editText?.text.toString())
        body.put("nomorhp",inpHPAkun.editText?.text.toString())
        val nmFile ="DC"+ SimpleDateFormat("yyyymmdd", Locale.getDefault()).format(Date())+".jpg"
        body.put("imgnasabah",nmFile)
        body.put("file",imstr)
        body.put("saldo","0")
        Log.d("nasabah", body.toString())
        return body
    }

    override fun onMapReady(p0: GoogleMap) {
        gMap = p0
        if(gMap!=null){
            airLoc = AirLocation(this,true,true,
                object : AirLocation.Callbacks{
                    override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                        Toast.makeText(this@NasabahAddActivity, "Gagal mendapatkan posisi saat ini",
                            Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(location: Location) {
                        val ll = LatLng(location.latitude,location.longitude)
                        gMap!!.addMarker(MarkerOptions().position(ll).title("Posisi Saya"))
                        gMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,16.0f))
                        lat= location.latitude

                        lng=location.longitude
                    }
                })
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLoc?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == photoHelper.getRcCamera() && selectMedia == false) {
                imstr = photoHelper.getBitMaoToString(imageViewAkun, fileUri)
                namaFile = photoHelper.getMyFileName()
                imageViewAkun.visibility = View.VISIBLE
            }
            if (requestCode == mediaHelper.getRcGallery() && selectMedia == true) {
                imstr = mediaHelper.getBitmapToString(data?.data, imageViewAkun)

                namaFile = "DC" + SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date()) + ".jpg"
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        airLoc?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
