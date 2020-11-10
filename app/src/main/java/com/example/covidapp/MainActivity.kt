package com.example.covidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covidapp.adapter.AdapterCountry
import com.example.covidapp.model.CountriesItem
import com.example.covidapp.model.ResponseCountry
import com.example.covidapp.network.ApiService
import com.example.covidapp.network.RetrofitBuilder.retrofit
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private var ascending =true
    companion object{
        private lateinit var adapters:AdapterCountry


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        search_view.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapters.filter.filter(newText)
                return false
            }



        })
        swipe_refresh.setOnRefreshListener {
            getNegara()
            swipe_refresh.isRefreshing =false
        }
        initializedView()
        getNegara()


    }

    private fun initializedView() {
        button_refresh.setOnClickListener {
            sequenzewithoutuinterner(ascending)
            ascending =!ascending
        }
    }

    private fun sequenzewithoutuinterner(ascending: Boolean) {
        rv_contry.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            if (ascending) {
                (layoutManager as LinearLayoutManager).reverseLayout = true
                (layoutManager as LinearLayoutManager).stackFromEnd = true
                Toast.makeText(this@MainActivity, "A - Z", Toast.LENGTH_SHORT).show()
            } else {
                (layoutManager as LinearLayoutManager).reverseLayout = false
                (layoutManager as LinearLayoutManager).stackFromEnd = false
                Toast.makeText(this@MainActivity, "Z - A", Toast.LENGTH_SHORT).show()

            }
            adapter = adapter
        }

    }


    private fun getNegara(){
        val api  =retrofit.create(ApiService::class.java)
        api.getAllNegara().enqueue(object :Callback<ResponseCountry>{
            override fun onFailure(call: Call<ResponseCountry>, t: Throwable) {
                progress_bar?.visibility =View.GONE
            }

            override fun onResponse(call: Call<ResponseCountry>, response: Response<ResponseCountry>
            ){
                if (response.isSuccessful){
                    val getlistdatacorona =response.body()!!.global
                    val formatter :NumberFormat =DecimalFormat("#,###")
                    txt_total_confirm.text = formatter.format(getlistdatacorona?.totalConfirmed?.toDouble())
                    txt_total_recovered.text = formatter.format(getlistdatacorona?.totalRecovered?.toDouble())
                    textView3.text = formatter.format(getlistdatacorona?.totalDeaths?.toDouble())
                    rv_contry.apply {
                        setHasFixedSize(true)
                        layoutManager =LinearLayoutManager(this@MainActivity)
                        progress_bar?.visibility =View.GONE
                        adapters =AdapterCountry(response.body()!!.countries as ArrayList<CountriesItem>){
                            negara ->itemClicked(negara) }


                            adapter =adapters


                    }
                }else{
                    progress_bar?.visibility=View.GONE
                }
            }
        })
    }

    private fun itemClicked(negara: CountriesItem) {
        val intentmove  =Intent(this@MainActivity,DetailChartCountry::class.java )
        intentmove.putExtra(DetailChartCountry.EXTRA_COUNTRY,negara)
        startActivity(intentmove)
    }
}