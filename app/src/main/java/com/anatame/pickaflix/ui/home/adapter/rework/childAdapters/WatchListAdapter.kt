package com.anatame.pickaflix.ui.home.adapter.rework.childAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.databinding.ItemHomeCategoryRvItemBinding
import com.anatame.pickaflix.databinding.ItemHomeWatchlistRvItemBinding
import com.anatame.pickaflix.utils.data.db.entities.Movie
import com.bumptech.glide.Glide

class WatchListAdapter(
    val context: Context,
    val homePosition: Int
) : RecyclerView.Adapter<WatchListAdapter.MovieItemViewHolder>() {

    inner class MovieItemViewHolder(val binding: ItemHomeWatchlistRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.source == newItem.source
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val binding = ItemHomeWatchlistRvItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Int, Movie, CardView) -> Unit)? = null
    private var onItemLongClickListener: ((Int, Movie, CardView) -> Unit)? = null

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.itemView.apply {

            holder.binding.apply {
                ViewCompat.setTransitionName(
                    cvCardContainer,
                    "iv${position}${homePosition}${movie.source}"
                )

                Glide.with(context)
                    .load(movie.thumbnailUrl)
                    .dontTransform()
                    .into(ivThumbnail)
            }

            setOnClickListener { view ->
                   onItemClickListener?.let { it(position, movie, holder.binding.cvCardContainer) }

            }

            setOnLongClickListener{ view ->
                onItemLongClickListener?.let { it(position, movie, holder.binding.cvCardContainer) }
                true
            }
        }
    }

    fun setOnItemClickListener(listener: (Int, Movie, CardView) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (Int, Movie, CardView) -> Unit) {
        onItemLongClickListener = listener
    }

}


