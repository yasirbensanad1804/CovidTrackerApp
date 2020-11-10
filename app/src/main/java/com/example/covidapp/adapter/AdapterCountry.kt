package com.example.covidapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.covidapp.R
import com.example.covidapp.model.CountriesItem
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.list_country.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterCountry (private val country:ArrayList<CountriesItem> , private val clickListener :(CountriesItem)-> Unit):
    RecyclerView.Adapter<CountriesViewHolder>() ,Filterable{

    var contrysitem = ArrayList<CountriesItem>()
    init {
        contrysitem =country
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.list_country,parent,false)
        return CountriesViewHolder(view)

    }
    override fun getItemCount(): Int {
        return contrysitem.size

    }
    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(contrysitem[position] ,clickListener)
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constrain: CharSequence?): FilterResults {
                val charSearch =constrain.toString()
                contrysitem =if (charSearch.isEmpty()){
                    country
                }else{
                    val resultList =ArrayList<CountriesItem>()
                    for (row in country){
                        val search = row.country?.toLowerCase(Locale.ROOT) ?:""
                        if (search.contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                }
                    resultList

            }
                val filterResult =FilterResults()
                filterResult.values =contrysitem
                return filterResult

            }

            override fun publishResults(p0: CharSequence?, result: FilterResults?) {
                contrysitem =result?.values as ArrayList<CountriesItem>
                notifyDataSetChanged()

            }

            }

        }
    }

class CountriesViewHolder(item:View):RecyclerView.ViewHolder(item) {
   fun bind(negara :CountriesItem ,clickListener: (CountriesItem) -> Unit){
       val country_corona : TextView =itemView.tv_country
       val flagNegara : CircleImageView =itemView.globe_flag_circle
       val total_cases : TextView =itemView.tv_totalcases
       val total_recovered : TextView =itemView.tv_totalRecovered
       val total_death : TextView =itemView.tv_totalDeath
       val formatter:NumberFormat =DecimalFormat("#,###")


       country_corona.tv_country.text =negara.country
       total_cases.tv_totalcases.text =formatter.format(negara.totalConfirmed?.toDouble())
       total_recovered.tv_totalRecovered.text =formatter.format(negara.totalRecovered?.toDouble())
       total_death.tv_totalDeath.text =formatter.format(negara.totalDeaths?.toDouble())

       Glide.with(itemView).load("https://www.countryflags.io/" + negara.countryCode +"/flat/64.png")
           .into(flagNegara)

       country_corona.setOnClickListener { clickListener(negara) }
       total_death.setOnClickListener { clickListener(negara) }
       total_cases.setOnClickListener { clickListener(negara) }
       total_recovered.setOnClickListener { clickListener(negara) }
       flagNegara.setOnClickListener { clickListener(negara) }

   }

}
