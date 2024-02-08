package com.app.tabunganuts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.tabunganuts.fragment.NasabahFragment
import com.app.tabunganuts.fragment.MoneyFragment
import com.app.tabunganuts.model.NasabahModel
import com.app.tabunganuts.model.MoneyModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        var listNasabahData = mutableListOf<NasabahModel>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setOnNavigationItemSelectedListener(onBottomNavListener)
        var fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.fragmentContainerView, MoneyFragment())
        fr.commit()
    }
    private val onBottomNavListener= BottomNavigationView.OnNavigationItemSelectedListener { i->
        when(i.itemId){
            R.id.menuTabungan ->{
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView, MoneyFragment())
                    commit()
                }
            }
            R.id.menuAkun ->{
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView, NasabahFragment())
                    commit()
                }
            }
        }
        true
    }
}
