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
import com.skillcinema.R
import com.skillcinema.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import skillcinema.data.PagedPremiereAdapter
import skillcinema.entity.Film


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private var pagedPremiereAdapter = PagedPremiereAdapter { "film -> onItemClick(film)" }

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

        binding.premiereRecyclerView.adapter = pagedPremiereAdapter
        Log.d("mytag","222222")
        viewModel.pagedPremiere.onEach {
            pagedPremiereAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

    private fun onItemClick(item: Film) {
//        val url = bundleOf("url" to item.posterUrlPreview)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}