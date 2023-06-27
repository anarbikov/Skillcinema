package skillcinema.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import kotlinx.android.synthetic.main.film_recyclerview.view.premiereRecyclerView
import kotlinx.android.synthetic.main.film_recyclerview.view.premiereShowAllLayout
import skillcinema.data.FilmsDto
import javax.inject.Inject

open class ParentFilmAdapter @Inject constructor() :
    RecyclerView.Adapter<ParentFilmAdapter.DataViewHolder>() {

    var filmCategoriesList: List<FilmsDto> = ArrayList()
    var onItemClick: ((FilmsDto) -> Unit)? = null

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                Log.d("mytag", "clicked")
                onItemClick?.invoke(filmCategoriesList[bindingAdapterPosition])
            }
        }

        @SuppressLint("SuspiciousIndentation")
        fun bind(result: FilmsDto) {
            val childMembersAdapter = ChildFilmAdapter(result.items.take(21))
            itemView.premiereRecyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            itemView.premiereRecyclerView.adapter = childMembersAdapter
            itemView.premiereRecyclerView.addItemDecoration(RecyclerItemDecoration(21, 8, false))
            itemView.premiereRecyclerView.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager =
                        LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                    val itemCount = recyclerView.layoutManager?.itemCount
                    val lastVisible = layoutManager!!.findLastVisibleItemPosition()
                    if (itemCount == lastVisible + 1) itemView.premiereShowAllLayout.visibility =
                        View.VISIBLE
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