package skillcinema.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillcinema.R
import com.skillcinema.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.parentRecyclerView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import skillcinema.data.FilmDto
import skillcinema.data.FilmsDto
import skillcinema.entity.Film
import skillcinema.entity.Films


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
//    private var pagedPremiereAdapter = PagedFilmAdapter { film -> onItemClick(film) }
    private lateinit var parentFilmAdapter: ParentFilmAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated { viewModel.checkForOnboarding() }
        if (viewModel.onboardingShownFlag == 0) findNavController().navigate(R.id.action_navigation_home_to_numberFragment)
        setUpViews()
        doObserveWork()


 //       setPremieresRecyclerView()

    }

//    private fun onItemClick(item: Film) {
//        val item = bundleOf("item" to item)
//        Log.d("mytag","clicked")
 //   }

//    private fun setPremieresRecyclerView() {
//        binding.premiereRecyclerView.adapter = pagedPremiereAdapter
//        binding.premiereRecyclerView.addItemDecoration(RecyclerItemDecoration(21, 8, false))
//        binding.homeTextViewPremieresAll.setOnClickListener{Log.d("mytag","clicked")}
//        viewModel.pagedPremiere.onEach {
//            pagedPremiereAdapter.submitData(it)
//        }.launchIn(viewLifecycleOwner.lifecycleScope)
//        binding.premiereRecyclerView.setOnClickListener { findNavController().navigate(R.id.action_navigation_home_to_numberFragment) }
//        binding.premiereRecyclerView.scrollToPosition(15)
//    }
private fun setUpViews() {

    parentFilmAdapter = ParentFilmAdapter()
    parentRecyclerView.adapter = parentFilmAdapter
    parentFilmAdapter
    parentRecyclerView.addItemDecoration(RecyclerItemDecoration(21, 8, true))
}

    private fun doObserveWork() {
        viewModel.movies.onEach {
parentFilmAdapter.addData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun renderGameOfThronesList(films: List<FilmsDto>) {
        parentFilmAdapter.addData(films)
        parentFilmAdapter.notifyDataSetChanged()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}