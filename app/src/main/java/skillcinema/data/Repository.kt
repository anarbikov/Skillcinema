package skillcinema.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import skillcinema.entity.Films
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: Api,
    @ApplicationContext
    val context: Context
) {
    private lateinit var prefs: SharedPreferences
    private var onboardingShown = 0
    fun getOnBoardingFlag(): Int {
        return when {
            onboardingShown == 1 -> 1
            getDataFromSharedPreference() == 1 -> {
                onboardingShown = 1
                1
            }
            else -> {
                saveOnboardingFlag()
                0
            }
        }
    }

    private fun getDataFromSharedPreference(): Int {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_INT_NAME, 0)

    }
    private fun saveOnboardingFlag() {
        onboardingShown = 1
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = prefs.edit()
        editor.putInt(KEY_INT_NAME,1)
        editor.apply()
    }

    suspend fun getPremieres(page: Int): Films {
        return api.getPremieres(page)
    }


    companion object{
        private  const val PREFERENCE_NAME = "prefs_name"
        private const val KEY_INT_NAME = "KEY_STRING"
    }
}