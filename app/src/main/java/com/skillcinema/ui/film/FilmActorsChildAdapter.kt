package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.databinding.FilmActorsViewBinding
import com.skillcinema.entity.ActorDto
import javax.inject.Inject

class FilmActorsChildAdapter @Inject constructor(
    info: List<ActorDto>
) : RecyclerView.Adapter<FilmActorsChildAdapter.ActorViewHolder>() {
    private var actorsList: List<ActorDto>
    init {
        actorsList = info
    }
    inner class ActorViewHolder(val binding:FilmActorsViewBinding) :
        RecyclerView.ViewHolder(binding.root){
        private val bundle = bundleOf()
            init {
                itemView.setOnClickListener{
                    bundle.putInt("staffId",actorsList[bindingAdapterPosition].staffId!!)
                    itemView.findNavController().navigate(R.id.action_filmFragment_to_actorFragment,bundle)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int) = ActorViewHolder(
        FilmActorsViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilmActorsChildAdapter.ActorViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(root.context)
                .load(actorsList[position].posterUrl)
                .centerCrop()
                .into(actorImageView)
            actorNameTextView.text = if (actorsList[position].nameRu != null)
                actorsList[position].nameRu else actorsList[position].nameEn ?: ""
            actorNickTextView.text = if (actorsList[position].description!=null) actorsList[position].description else actorsList[position].professionText ?: ""
        }
    }

    override fun getItemCount(): Int {
        return actorsList.size
    }
}
