//package skillcinema.data
//
//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.skillcinema.databinding.FilmViewBinding
//import skillcinema.entity.Film
//import javax.inject.Inject
//
//
//class PagedFilmAdapter @Inject constructor(
//    private val onClick: (Film) -> Unit
//) : PagingDataAdapter<Film, FilmViewHolder>(DiffUtilCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
//        return FilmViewHolder(
//            FilmViewBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    @SuppressLint("ResourceAsColor")
//    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
//        val item = getItem(position)
//        val posterPreviewUrl = item?.posterUrlPreview
//        Glide.with(holder.itemView.context).load(posterPreviewUrl)
//            .into(holder.binding.filmImageView)
//        holder.binding.filmNameTextView.text = item?.nameRu
//        var genres = ""
//        if (item?.genres?.size!! == 1) genres += item.genres[0].genre
//        else {
//            for (i in item.genres) {
//                genres += i.genre + ", "
//            }
//        }
//        holder.binding.filmGenreTextView.text =
//            if (item.genres.size != 1) genres.dropLast(2) else genres
//        holder.binding.root.setOnClickListener {
//            onClick(item)
//        }
//    }
//}
//
//class DiffUtilCallback : DiffUtil.ItemCallback<Film>() {
//    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean =
//        oldItem.kinopoiskId == newItem.kinopoiskId
//
//    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean =
//        oldItem.kinopoiskId == newItem.kinopoiskId
//}
//
//class FilmViewHolder(val binding: FilmViewBinding) : RecyclerView.ViewHolder(binding.root)