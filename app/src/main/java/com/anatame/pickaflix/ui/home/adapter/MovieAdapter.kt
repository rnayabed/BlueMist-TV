package com.anatame.pickaflix.ui.home.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.R
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.databinding.ItemHomeCategoryRvItemBinding

class MovieAdapter(
    val context: Context
) : RecyclerView.Adapter<MovieAdapter.MovieItemViewHolder>() {

    inner class MovieItemViewHolder(val binding: ItemHomeCategoryRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<MovieItem>() {
        override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem.Url == newItem.Url
        }

        override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val binding = ItemHomeCategoryRvItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Int, MovieItem, CardView) -> Unit)? = null

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        val movieItem = differ.currentList[position]
        holder.itemView.apply {

            holder.binding.apply {
                ViewCompat.setTransitionName(
                    cvCardContainer,
                    "iv$position ${movieItem.Url}"
                )
            }

            setOnClickListener { view ->
                   onItemClickListener?.let { it(position, movieItem, holder.binding.cvCardContainer) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Int, MovieItem, CardView) -> Unit) {
        onItemClickListener = listener
    }


}


