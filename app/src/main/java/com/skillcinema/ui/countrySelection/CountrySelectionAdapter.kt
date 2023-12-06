package com.skillcinema.ui.countrySelection

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.data.FilterCountryDto
import com.skillcinema.databinding.CountrySelectionCountryViewBinding
import kotlinx.android.synthetic.main.country_selection_country_view.view.countryName
import javax.inject.Inject

class CountrySelectionAdapter @Inject constructor(
var onClickCountry: (Int) -> Unit

):
RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var countries: List<FilterCountryDto> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
        val view = CountrySelectionCountryViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = countries[position]
        holder.itemView.apply {
            countryName.text = result.country
                setOnClickListener {
                    result.id?.let { it1 -> onClickCountry(it1) }
            }
        }
    }

    override fun getItemCount(): Int = countries.size

    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<FilterCountryDto>) {
        countries = list
        notifyDataSetChanged()
    }
}
class ViewHolder(binding: CountrySelectionCountryViewBinding) :
    RecyclerView.ViewHolder(binding.root)