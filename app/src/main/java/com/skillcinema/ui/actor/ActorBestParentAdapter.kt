package com.skillcinema.ui.actor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.entity.ActorGeneralInfoDto
import com.skillcinema.entity.FilmInfo
import kotlinx.android.synthetic.main.actor_best_child_rv.view.actorBestAll
import kotlinx.android.synthetic.main.actor_best_child_rv.view.actorBestHeader
import kotlinx.android.synthetic.main.actor_best_child_rv.view.childRecyclerViewBest
import javax.inject.Inject

class ActorBestParentAdapter @Inject constructor(
    val context: Context,
) : RecyclerView.Adapter<ActorBestParentAdapter.ViewHolderGallery>() {
    private lateinit var best: List<FilmInfo>
    private lateinit var generalInfo: ActorGeneralInfoDto
    inner class ViewHolderGallery(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(result:List< FilmInfo>) {
            itemView.childRecyclerViewBest.adapter =
                ActorBestChildAdapter(best,context,generalInfo)
            itemView.childRecyclerViewBest.layoutManager =
                GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            itemView.actorBestAll.text = if(result.size>10) "${context.getString(R.string.all)} >" else ""
            itemView.actorBestAll.visibility = if (result.isNotEmpty())View.VISIBLE else View.GONE
            val header = context.getString(R.string.best)
            itemView.actorBestHeader.text = if (result.isNotEmpty()) header else ""
            itemView.actorBestHeader.visibility = if (result.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGallery = ViewHolderGallery (
        LayoutInflater.from(parent.context).inflate(
            R.layout.actor_best_child_rv, parent,
            false
        )
    )
    override fun onBindViewHolder(holder: ViewHolderGallery, position: Int) {
        holder.bind(best)
    }

    override fun getItemCount(): Int = 1

    @SuppressLint("NotifyDataSetChanged")
    fun addData(info: List<FilmInfo>,general:ActorGeneralInfoDto) {
        best = info
        generalInfo = general
        notifyDataSetChanged()
    }
}