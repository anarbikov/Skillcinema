package com.skillcinema.ui.countrySelection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.skillcinema.data.FilmFilters
import com.skillcinema.databinding.FragmentCountrySelectionBinding
import com.skillcinema.ui.search.SearchSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountrySelection : Fragment() {

    private var _binding: FragmentCountrySelectionBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountrySelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CountrySelectionAdapter{countryId -> onClickCountry(countryId = countryId)}
        binding.recyclerView.adapter = adapter
        val countries = FilmFilters.getAllCountries().filter { it.country != "" }.sortedBy { it.country }
        adapter.addData(countries)

        binding.goBack.setOnClickListener { findNavController().popBackStack() }

    }

    private fun onClickCountry(countryId:Int){
        SearchSettings.countries = listOf(countryId)
        findNavController().popBackStack()
    }
}