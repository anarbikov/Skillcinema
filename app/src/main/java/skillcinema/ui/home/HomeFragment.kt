package skillcinema.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.skillcinema.R
import com.skillcinema.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var prefs: SharedPreferences
    private var onboardingShown: Int = 0
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onboardingShown = getDataFromSharedPreference()
        if (onboardingShown ==0) {
            saveOnboardingFlag()
            findNavController().navigate(R.id.action_navigation_home_to_numberFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun getDataFromSharedPreference(): Int {
        prefs = requireContext().getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
        return prefs.getInt(KEY_INT_NAME, 0)
    }
    private fun saveOnboardingFlag(){
        onboardingShown = 1
        prefs = requireContext().getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = prefs.edit()
        editor.putInt(KEY_INT_NAME,1)
        editor.apply()
    }
    companion object{
        private  const val PREFERENCE_NAME = "prefs_name"
        private const val KEY_INT_NAME = "KEY_STRING"
    }
}