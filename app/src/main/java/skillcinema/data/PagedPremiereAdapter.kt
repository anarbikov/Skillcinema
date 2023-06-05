package skillcinema.data

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.FilmViewBinding
import skillcinema.entity.Film
import javax.inject.Inject

class PagedPremiereAdapter @Inject constructor(
    private val onClick: (Film) -> Unit
) : PagingDataAdapter<Film, PremiereViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremiereViewHolder {
        return PremiereViewHolder(
            FilmViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: PremiereViewHolder, position: Int) {
        val item = getItem(position)
        val posterPreviewUrl = item?.posterUrlPreview
        Glide.with(holder.itemView.context).load(posterPreviewUrl).into(holder.binding.filmImageView)

//        holder.binding.filmNameTextView.text = item?.kinopoiskId.toString()
    }

}
class DiffUtilCallback : DiffUtil.ItemCallback<Film>() {
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId
}
class PremiereViewHolder(val binding: FilmViewBinding): RecyclerView.ViewHolder(binding.root)