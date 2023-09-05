package com.skillcinema.ui.actor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillcinema.databinding.FragmentActorBinding
import com.skillcinema.entity.ActorGeneralInfoDto
import com.skillcinema.entity.FilmInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActorFragment : Fragment() {

    private var _binding: FragmentActorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActorViewModel by viewModels()
    private lateinit var actorGeneralInfoAdapter: ActorGeneralInfoAdapter
    private lateinit var actorBestParentAdapter: ActorBestParentAdapter
    private lateinit var filmographyAdapter: ActorFilmographyAdapter
    private lateinit var concatAdapter: ConcatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val staffId = arguments.let { it?.getInt("staffId") }
        setUpViews()
        doObserveWork()
    }
    private fun doObserveWork() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    when (it) {
                        true -> {
                            binding.concatRecyclerView.visibility = View.GONE
                            binding.loadingProgress.visibility = View.VISIBLE
                            requireActivity().nav_view.visibility = View.GONE
                        }

                        else -> {
                            binding.concatRecyclerView.visibility = View.VISIBLE
                            binding.loadingProgress.visibility = View.GONE
                            requireActivity().nav_view.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        viewModel.actorInfo.onEach {
            setupAndRenderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    @Suppress("UNCHECKED_CAST")
    private fun setupAndRenderView (allInfo:List<Any>){
        if (allInfo.isEmpty())return
        val isLoaded = allInfo[0] as Boolean
        if (!isLoaded) {
            binding.loadingErrorPage.visibility = View.VISIBLE
            binding.button.setOnClickListener { findNavController().popBackStack() }
            return
        }
        else binding.loadingErrorPage.visibility = View.GONE
        viewModel.checkWatched()
        val generalInfo: ActorGeneralInfoDto = allInfo[1] as ActorGeneralInfoDto
        actorGeneralInfoAdapter.addData(generalInfo)
        val filmListByActor:List<FilmInfo> = allInfo[2] as List<FilmInfo>
        actorBestParentAdapter.addData(filmListByActor,generalInfo)
        filmographyAdapter.addData(generalInfo)
        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(true)
        }.build()
        concatAdapter = ConcatAdapter(config, actorGeneralInfoAdapter,actorBestParentAdapter,filmographyAdapter)
        binding.concatRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.concatRecyclerView.adapter = concatAdapter
    }
    private fun setUpViews(){
        actorGeneralInfoAdapter = ActorGeneralInfoAdapter(requireContext())
        actorBestParentAdapter = ActorBestParentAdapter(requireContext())
        filmographyAdapter = ActorFilmographyAdapter(requireContext())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}