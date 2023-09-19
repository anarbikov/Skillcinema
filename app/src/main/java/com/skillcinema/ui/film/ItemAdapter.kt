package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.FragmentCollectionDialogAddItemsBinding
import com.skillcinema.databinding.FragmentCollectionDialogCheckItemBinding
import com.skillcinema.databinding.FragmentCollectionDialogCreateCollectionBinding
import com.skillcinema.databinding.FragmentCollectionDialogTitleBinding
import com.skillcinema.entity.FilmInfo
import kotlinx.android.synthetic.main.fragment_collection_dialog_check_item.view.collectionTitle
import kotlinx.android.synthetic.main.fragment_collection_dialog_check_item.view.collectionsSize
import kotlinx.android.synthetic.main.fragment_collection_dialog_title.view.ratingTextView
import kotlinx.android.synthetic.main.fragment_collection_dialog_title.view.titleBodyTextView
import kotlinx.android.synthetic.main.fragment_collection_dialog_title.view.titleImageView
import kotlinx.android.synthetic.main.fragment_collection_dialog_title.view.titleTextView


class ItemAdapter(

    private val filmInfo: FilmInfo,
    private val onClickAddToCollection: (Pair<String,Boolean>) -> Unit,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var collections : List<CollectionData> = ArrayList()
    private val checkMap = mutableMapOf<String,Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
                 TITLE -> ViewHolder1( FragmentCollectionDialogTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
                TYPE_ADD_TO_COLLECTION -> ViewHolder2 (FragmentCollectionDialogAddItemsBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
                TYPE_CREATE_COLLECTION -> ViewHolder3 (FragmentCollectionDialogCreateCollectionBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
                else -> ViewHolder4( FragmentCollectionDialogCheckItemBinding .inflate(
                    LayoutInflater.from(parent.context),parent, false))
            }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_COLLECTION_ROW -> {
                checkMap[collections[position-2].collectionName] = false
                holder.itemView.apply {
                    collectionTitle.text = collections[position-2].collectionName
                    collectionsSize.text = collections[position-2].collectionsSize.toString()
                    collectionTitle.isChecked = collections[position-2].isInCollection
                    collectionTitle.setOnCheckedChangeListener{ button, checked ->
                        checkMap[button.text as String] = checked
                        onClickAddToCollection (Pair(collectionTitle.text.toString(),checked))
                    }
                }
            }
            TYPE_ADD_TO_COLLECTION -> {}

            TYPE_CREATE_COLLECTION -> {
                holder.itemView.apply {  }
            }

            TITLE -> {
                holder.itemView.apply {
                    Glide.with(context)
                        .load(filmInfo.posterUrlPreview?:filmInfo.posterUrl)
                        .centerCrop()
                        .into(titleImageView)
                    titleTextView.text = (filmInfo.nameRu?:filmInfo.nameEn?:filmInfo.nameOriginal?:"") as CharSequence?
                    titleBodyTextView.text = "${if (filmInfo.year!=null) filmInfo.year.toString()+"," 
                    else ""} ${filmInfo.genres.joinToString(", "){ it.genre.toString() }}"
                    ratingTextView.text = (filmInfo.ratingKinopoisk?:"").toString()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TITLE
            1 -> TYPE_ADD_TO_COLLECTION
            (itemCount-1) -> TYPE_CREATE_COLLECTION
            else -> TYPE_COLLECTION_ROW
        }
    }
    override fun getItemCount(): Int = collections.size +3

    @SuppressLint("NotifyDataSetChanged")
    fun removeData (){
        this.collections = listOf()
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData (info:List<CollectionData>) {
        this.collections = info
        notifyDataSetChanged()
    }

    companion object{
        private const val TITLE = 0
        private const val TYPE_ADD_TO_COLLECTION = 1
        private const val TYPE_COLLECTION_ROW = 2
        private const val TYPE_CREATE_COLLECTION = 3

    }
}
class ViewHolder1(binding: FragmentCollectionDialogTitleBinding) :
    RecyclerView.ViewHolder(binding.root)
class ViewHolder2(binding: FragmentCollectionDialogAddItemsBinding) :
    RecyclerView.ViewHolder(binding.root)
class ViewHolder3(binding: FragmentCollectionDialogCreateCollectionBinding) :
    RecyclerView.ViewHolder(binding.root)
class ViewHolder4(binding: FragmentCollectionDialogCheckItemBinding) :
    RecyclerView.ViewHolder(binding.root)