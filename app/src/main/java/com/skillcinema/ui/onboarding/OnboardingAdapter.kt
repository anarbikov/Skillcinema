package com.skillcinema.ui.onboarding

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.databinding.FragmentOnboardingPagerBinding
import javax.inject.Inject

class OnboardingAdapter @Inject constructor(
    private val context: Context
    ) : RecyclerView.Adapter<OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(
            FragmentOnboardingPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {

        holder.binding.onBoardingTextView.text = when (position+1) {
            1 -> context.resources.getString(R.string.onboarding_1_text)
            2 -> context.resources.getString(R.string.onboarding_2_text)
            3 -> context.resources.getString(R.string.onboarding_3_text)
            else -> context.resources.getString(R.string.onboarding_1_text)
        }
        holder.binding.onBoardingTextView.setTextColor(R.color.black)
        holder.binding.onBoardingImageView.setImageResource(
            when (position+1) {
                1 -> R.drawable.ic_onboarding_1
                2 -> R.drawable.ic_onboarding_2
                3 -> R.drawable.ic_onboarding_3
                else -> R.drawable.ic_onboarding_1
            }
        )
        holder.binding.onBoardingScreenNumber.setImageResource(
            when (position+1) {
                1 -> R.drawable.ic_onboarding_screen_number
                2 -> R.drawable.ic_onboarding_screen_number2
                3 -> R.drawable.ic_onboarding_screen_number3
                else -> R.drawable.ic_onboarding_screen_number
            }
        )
    }

    override fun getItemCount(): Int = 3
}

class OnboardingViewHolder(val binding: FragmentOnboardingPagerBinding): RecyclerView.ViewHolder(binding.root)