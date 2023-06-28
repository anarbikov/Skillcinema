package skillcinema.data

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.databinding.FilmViewBinding
import kotlinx.android.synthetic.main.film_recyclerview.view.premiereRecyclerView
import kotlinx.android.synthetic.main.film_recyclerview.view.premiereShowAllLayout
import skillcinema.entity.Film
import skillcinema.ui.home.RecyclerItemDecoration
import javax.inject.Inject


class PagedFilmAdapter @Inject constructor(
    private val onClick: (Film) -> Unit
) : PagingDataAdapter<Film, FilmViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            FilmViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val item = getItem(position)
        val posterPreviewUrl = item?.posterUrlPreview
        Glide.with(holder.itemView.context).load(posterPreviewUrl)
            .into(holder.binding.filmImageView)
        holder.binding.filmNameTextView.text = item?.nameRu
        var genres = ""
        if (item?.genres?.size!! == 1) genres += item.genres[0].genre
        else {
            for (i in item.genres) {
                genres += i.genre + ", "
            }
        }
        holder.binding.filmGenreTextView.text =
            if (item.genres.size != 1) genres.dropLast(2) else genres
        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<Film>() {
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId
}

class FilmViewHolder(val binding: FilmViewBinding) : RecyclerView.ViewHolder(binding.root)
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
            val childMembersAdapter = ChildFilmAdapter(result.items.take(21).shuffled())
            itemView.premiereRecyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            itemView.premiereRecyclerView.adapter = childMembersAdapter
            itemView.premiereRecyclerView.addItemDecoration(RecyclerItemDecoration(21, 8, true))
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