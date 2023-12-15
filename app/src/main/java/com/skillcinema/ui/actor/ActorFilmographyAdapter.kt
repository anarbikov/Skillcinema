package com.skillcinema.ui.actor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.databinding.ActorFilmographyViewBinding
import com.skillcinema.entity.ActorGeneralInfoDto
import javax.inject.Inject

class ActorFilmographyAdapter @Inject constructor(
    val context: Context
) : RecyclerView.Adapter<ActorFilmographyAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ActorFilmographyViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private lateinit var filmsInfo: ActorGeneralInfoDto
    private val bundle = bundleOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            ActorFilmographyViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            filmographyHeader.text = if (filmsInfo.films?.size !=0) context.getString(R.string.filmography_header) else ""
            filmographyHeader.visibility = if (filmsInfo.films?.size !=0) View.VISIBLE else View.GONE
            filmographyAll.text = if(filmsInfo.films?.size !=0) context.getString(R.string.filmography_all) else ""
            filmographyAll.visibility = if (filmsInfo.films?.size !=0)View.VISIBLE else View.GONE
            val filmQty:String = if (filmsInfo.films?.size !=0) filmsInfo.films?.size.toString() else ""
            val film = when(filmsInfo.films!!.size%10){
                1 -> "фильм"
                in 2..4 -> "фильма"
                in 5..9, 0 -> "фильмов"
                else -> "фильмов"
            }
            val description = "$filmQty $film"
            filmographyDescription.text = if (filmsInfo.films?.size !=0) description else ""
            filmographyDescription.visibility= if (filmsInfo.films?.size !=0) View.VISIBLE else View.GONE
            filmographyAll.setOnClickListener {
                bundle.putInt("staffId",filmsInfo.personId!!)
                it.findNavController().navigate(R.id.action_actorFragment_to_filmographyFragment,bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return 1
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData(info: ActorGeneralInfoDto) {
        this.filmsInfo = info
        notifyDataSetChanged()
    }
}