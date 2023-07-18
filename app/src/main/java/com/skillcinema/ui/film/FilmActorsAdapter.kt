package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.entity.ActorDto
import kotlinx.android.synthetic.main.actors_child_rv.view.actorsAll
import kotlinx.android.synthetic.main.actors_child_rv.view.actorsHeader
import kotlinx.android.synthetic.main.actors_child_rv.view.childRecyclerView
import javax.inject.Inject

class FilmActorsAdapter @Inject constructor(
    val context: Context,
) : RecyclerView.Adapter<FilmActorsAdapter.ViewHolderActors>() {
    private lateinit var actorsList: List<ActorDto>
    private lateinit var otherStaff: List<ActorDto>

    inner class ViewHolderActors(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(result: List<ActorDto>, position: Int) {
            val takeSize = if (position == 0) 12 else 6
            itemView.childRecyclerView.adapter =
                FilmActorsChildAdapter(context, result.take(takeSize))
            val spanCount = if (position == 0) 4 else 2

            itemView.childRecyclerView.layoutManager =
                GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false)
            itemView.actorsAll.text = "${result.size} >"
            itemView.actorsHeader.text =
                if (position == 0) context.getString(R.string.actors_header) else context.getString(
                    R.string.other_staff_header
                )
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderActors = ViewHolderActors (
        LayoutInflater.from(parent.context).inflate(
            R.layout.actors_child_rv, parent,
            false
        )
    )


    override fun onBindViewHolder(holder: ViewHolderActors, position: Int) {
        if (position == 0) holder.bind(actorsList,position) else holder.bind(otherStaff,position)
    }

    override fun getItemCount(): Int = 2
    @SuppressLint("NotifyDataSetChanged")
    fun addData(actors: List<ActorDto>,others: List<ActorDto>) {
        actorsList = actors
        otherStaff = others
        notifyDataSetChanged()
    }
}