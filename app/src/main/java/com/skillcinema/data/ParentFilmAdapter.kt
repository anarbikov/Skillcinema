package com.skillcinema.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.customview.widget.ViewDragHelper.STATE_IDLE
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.ui.home.RecyclerItemDecoration
import kotlinx.android.synthetic.main.film_recyclerview.view.childRecyclerView
import kotlinx.android.synthetic.main.film_recyclerview.view.homeTextViewPremieres
import kotlinx.android.synthetic.main.film_recyclerview.view.premiereShowAllLayout
import me.everything.android.ui.overscroll.IOverScrollState.STATE_BOUNCE_BACK
import me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_END_SIDE
import me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_START_SIDE
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import javax.inject.Inject

open class ParentFilmAdapter @Inject constructor(
    val context: Context
) :
    RecyclerView.Adapter<ParentFilmAdapter.DataViewHolder>() {

    var filmCategoriesList: List<FilmsDto> = listOf()
    var onItemClick: ((FilmsDto) -> Unit)? = null


    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("id", filmCategoriesList[bindingAdapterPosition].filterCategory!!)
                bundle.putString("description",filmCategoriesList[bindingAdapterPosition].category)
//                Log.d("mytag","parentfilterid: ${filmCategoriesList[bindingAdapterPosition].filterCategory!!}")
                itemView.findNavController().navigate(R.id.action_navigation_home_to_fullFilmList,bundle)
//                onItemClick?.invoke(filmCategoriesList[bindingAdapterPosition])
            }

        }

        @SuppressLint("SuspiciousIndentation")
        fun bind(result: FilmsDto) {
            itemView.homeTextViewPremieres.text = result.category
            val childMembersAdapter = ChildFilmAdapter(result.category!!,result.items.take(20), context)
            val footerAdapter = FooterAdapter()
            itemView.childRecyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val concatAdapter = ConcatAdapter(childMembersAdapter,footerAdapter)
            itemView.childRecyclerView.adapter = concatAdapter

            if (itemView.childRecyclerView.itemDecorationCount == 0) {
                itemView.childRecyclerView.addItemDecoration(RecyclerItemDecoration(21, 8, true))
            }
            val decorator = OverScrollDecoratorHelper.setUpOverScroll(itemView.childRecyclerView,OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)
            decorator.setOverScrollStateListener { _, _, newState ->
                when (newState) {
                    STATE_DRAG_END_SIDE -> {
                        val bundle = Bundle()
                        bundle.putInt("id", filmCategoriesList[bindingAdapterPosition].filterCategory!!)
                        bundle.putString("description",filmCategoriesList[bindingAdapterPosition].category)
//                        Log.d("mytag","parentfilterid: ${filmCategoriesList[bindingAdapterPosition].filterCategory!!}")
                        itemView.findNavController().navigate(R.id.action_navigation_home_to_fullFilmList,bundle)
                    }
                }
            }
            itemView.childRecyclerView.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager =
                        LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                    val itemCount = recyclerView.layoutManager?.itemCount
                    val lastVisible = layoutManager!!.findLastVisibleItemPosition()
             if (itemCount == lastVisible + 1) {
                        itemView.premiereShowAllLayout.visibility =
                            View.VISIBLE
                    }
                    else itemView.premiereShowAllLayout.visibility = View.GONE
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.film_recyclerview, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(filmCategoriesList[position])
    }
    override fun getItemCount(): Int = filmCategoriesList.size
    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<FilmsDto>) {
        filmCategoriesList = list
        notifyDataSetChanged()
    }
}