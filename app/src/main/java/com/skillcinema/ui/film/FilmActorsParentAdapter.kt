package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.entity.ActorDto
import kotlinx.android.synthetic.main.film_actors_child_rv.view.actorsAll
import kotlinx.android.synthetic.main.film_actors_child_rv.view.actorsHeader
import kotlinx.android.synthetic.main.film_actors_child_rv.view.childRecyclerView
import javax.inject.Inject

class FilmActorsParentAdapter @Inject constructor(
    val context: Context,
) : RecyclerView.Adapter<FilmActorsParentAdapter.ViewHolderActors>() {
    private var actorsList: List<ActorDto> = listOf()
    private var otherStaff: List<ActorDto> = listOf()
    private var isSeries = false
    private val bundle = bundleOf()
    private var kinopoiskFilmId = 0

    inner class ViewHolderActors(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(result: List<ActorDto>, position: Int) {
            val takeSize = if (position == 0) 12 else 6
            val takenStaff = result.take(takeSize)
            itemView.childRecyclerView.adapter =
                FilmActorsChildAdapter(context, takenStaff)
            val spanCount = if (position == 0) 4 else 2
            val spanCountActual = when {
                result.isEmpty() -> 1
                result.size > spanCount -> spanCount
                else -> result.size
            }
            itemView.childRecyclerView.layoutManager =
                GridLayoutManager(context, spanCountActual, GridLayoutManager.HORIZONTAL, false)
            itemView.actorsAll.text = if(takenStaff.size<result.size) "${result.size} >" else ""
            itemView.actorsAll.visibility = if (result.isNotEmpty())View.VISIBLE else View.GONE
            itemView.actorsAll.setOnClickListener{
                bundle.putInt("kinopoiskId",kinopoiskFilmId)
                bundle.putBoolean("isActor", position == 0)
                bundle.putBoolean("isSeries",isSeries)
                it.findNavController().navigate(R.id.action_filmFragment_to_actorsFullFragment,bundle)
            }
            val header = when {
                position == 0 && isSeries -> context.getString(R.string.series_actors_header)
                position == 0 && !isSeries -> context.getString(R.string.actors_header)
                position == 1 && isSeries -> context.getString(R.string.series_other_staff_header)
                position == 1 && !isSeries -> context.getString(R.string.other_staff_header)
                else -> ""
            }
            itemView.actorsHeader.text = if (result.isNotEmpty()) header else ""
            itemView.actorsHeader.visibility = if (result.isNotEmpty()) View.VISIBLE else View.GONE

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderActors = ViewHolderActors (
        LayoutInflater.from(parent.context).inflate(
            R.layout.film_actors_child_rv, parent,
            false
        )
    )
    override fun onBindViewHolder(holder: ViewHolderActors, position: Int) {
        if (position == 0) holder.bind(actorsList,position) else holder.bind(otherStaff,position)
    }

    override fun getItemCount(): Int = 2
    @SuppressLint("NotifyDataSetChanged")
    fun addData(actors: List<ActorDto>,others: List<ActorDto>,series: Boolean,filmId:Int) {
        actorsList = actors
        otherStaff = others
        isSeries = series
        kinopoiskFilmId = filmId
        notifyDataSetChanged()
    }
}