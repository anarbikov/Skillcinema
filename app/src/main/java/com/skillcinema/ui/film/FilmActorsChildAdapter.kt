package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.entity.ActorDto
import kotlinx.android.synthetic.main.film_actors_view.view.actorImageView
import kotlinx.android.synthetic.main.film_actors_view.view.actorNameTextView
import kotlinx.android.synthetic.main.film_actors_view.view.actorNickTextView
import javax.inject.Inject

class FilmActorsChildAdapter @Inject constructor(
    val context: Context,
    info: List<ActorDto>
) : RecyclerView.Adapter<FilmActorsChildAdapter.ActorViewHolder>() {
    private var actorsList: List<ActorDto>
    init {
        actorsList = info
    }
    inner class ActorViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView){
        private val bundle = bundleOf()
            init {
                itemView.setOnClickListener{
                    bundle.putInt("staffId",actorsList[bindingAdapterPosition].staffId!!)
                    itemView.findNavController().navigate(R.id.action_filmFragment_to_actorFragment,bundle)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int) = ActorViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.film_actors_view,
            parent,
            false
        )
    )
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilmActorsChildAdapter.ActorViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(holder.itemView.context)
                .load(actorsList[position].posterUrl)
                .centerCrop()
                .into(this.actorImageView)
            this.actorNameTextView.text = if (actorsList[position].nameRu != null)
                actorsList[position].nameRu else actorsList[position].nameEn ?: ""
            this.actorNickTextView.text = if (actorsList[position].description!=null) actorsList[position].description else actorsList[position].professionText ?: ""
        }
    }

    override fun getItemCount(): Int {
        return actorsList.size
    }
}
