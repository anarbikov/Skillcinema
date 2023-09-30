package com.skillcinema.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.databinding.FragmentProfileFilmParentAdapterBinding
import com.skillcinema.room.CollectionWIthFilms
import com.skillcinema.room.Film
import kotlinx.android.synthetic.main.fragment_profile_film_parent_adapter.view.all
import kotlinx.android.synthetic.main.fragment_profile_film_parent_adapter.view.collectionRecyclerView
import kotlinx.android.synthetic.main.fragment_profile_film_parent_adapter.view.header
import javax.inject.Inject

class ParentFilmListAdapter @Inject constructor(
    val context: Context,
    var onClickOpenFullCollection: (CollectionWIthFilms) -> Unit,
    var onClickCLeanCollection: (String) -> Unit,
    var onClickFilm: (Film) -> Unit

):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var collections: List<CollectionWIthFilms> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
        return  ViewHolder(
                FragmentProfileFilmParentAdapterBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ))
    }

    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = collections[0]
        val takenFilms = if (result.films.size < 20) result.films else result.films.take(20)
        val childFilmListAdapter =
            ChildFilmListAdapter(
                context = context,
                filmData = collections,
                onClickCleanHistory = {cleanCollection ->
                    onClickCLeanCollection(cleanCollection)
                    holder.itemView.all.text = "0"
                    holder.itemView.collectionRecyclerView.visibility = View.GONE
                },
                onClickFilm = { film -> onClickFilm(film)}
            )
                holder.itemView.apply {
                    collectionRecyclerView.visibility = if (result.films.isNotEmpty()) View.VISIBLE else View.GONE
                    header.text = result.collection.name
                    all.text = "${takenFilms.size}  >"
                    collectionRecyclerView.adapter = childFilmListAdapter
                    all.setOnClickListener {
                        onClickOpenFullCollection(result)
                    }
                }
            }

    override fun getItemCount(): Int = collections.size

    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<CollectionWIthFilms>) {
        collections = list
        notifyDataSetChanged()
    }
}
class ViewHolder(binding: FragmentProfileFilmParentAdapterBinding) :
    RecyclerView.ViewHolder(binding.root)