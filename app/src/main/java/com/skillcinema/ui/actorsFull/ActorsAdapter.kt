package com.skillcinema.ui.actorsFull

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.databinding.ActorsFullActorViewBinding
import com.skillcinema.entity.ActorDto
import kotlinx.android.synthetic.main.film_actors_view.view.actorImageView
import kotlinx.android.synthetic.main.film_actors_view.view.actorNameTextView
import kotlinx.android.synthetic.main.film_actors_view.view.actorNickTextView
import javax.inject.Inject

class ActorsAdapter @Inject constructor(
    val context: Context,
) : RecyclerView.Adapter<ActorsAdapter.ActorViewHolder>() {
    private var actorsList: List<ActorDto> = listOf()

    inner class ActorViewHolder( val binding: ActorsFullActorViewBinding) :
        RecyclerView.ViewHolder(binding.root){
        private val bundle = bundleOf()
        init {
            itemView.setOnClickListener{
                bundle.putInt("staffId",actorsList[bindingAdapterPosition].staffId!!)
                itemView.findNavController().navigate(R.id.action_actorsFullFragment_to_actorFragment,bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ActorViewHolder {
        val view = ActorsFullActorViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        view.root.layoutParams.width = screenWidth / 2-20

        return ActorViewHolder(view)


    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ActorsAdapter.ActorViewHolder, position: Int) {
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
    @SuppressLint("NotifyDataSetChanged")
    fun addData (actors:List<ActorDto>) {
        this.actorsList = actors
        notifyDataSetChanged()
    }
}
