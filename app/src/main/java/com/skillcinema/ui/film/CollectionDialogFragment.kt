package com.skillcinema.ui.film

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skillcinema.databinding.FragmentCollectionDialogListDialogBinding
import com.skillcinema.entity.FilmInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

const val ARG_FILM_INFO = "filmInfo"


@AndroidEntryPoint
class CollectionDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCollectionDialogListDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DialogViewModel by viewModels()
    private lateinit var filmInfo: FilmInfo
    private lateinit var adapter: ItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        filmInfo = arguments?.getParcelable(ARG_FILM_INFO)!!
        binding.list.layoutManager = GridLayoutManager(requireContext(), 1)
        adapter = ItemAdapter(
            filmInfo = filmInfo,
            onClickAddToCollection = { add -> onClickAddToCollections(add) }
        )

        binding.list.adapter = adapter
        doObserveWork()
        binding.closeButton.setOnClickListener { findNavController().popBackStack() }
    }

    private fun doObserveWork() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
            }
        }
        viewModel.collection.receiveAsFlow().onEach {
            renderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
     }
    private fun renderView(collections: List<CollectionData>){
        if (collections.isEmpty()) return
        else {
            adapter.removeData()
            adapter.addData(collections)
        }
    }

    private fun onClickAddToCollections(checked: Pair<String, Boolean>) {
        if (checked.second) {
            viewModel.addToCollections(checkedCollection = checked.first, filmInfo = filmInfo)
        } else {
            viewModel.deleteFromCollection(
                collection = checked.first,
                filmId = filmInfo.kinopoiskId!!
            )
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("mytag","stop")
        parentFragmentManager.setFragmentResult("update", bundleOf())
    }
}