package skillcinema.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import skillcinema.domain.GetSharedPrefsUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSharedPrefsUseCase: GetSharedPrefsUseCase
) : ViewModel() {
    var onboardingShownFlag: Int = 0

    fun checkForOnboarding() {
        onboardingShownFlag = getSharedPrefsUseCase.execute()
    }
}