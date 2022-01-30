package com.anatame.pickaflix.ui.home.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.databinding.ItemHomeCategoryRvItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

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

                Glide.with(context)
                    .load(movieItem.thumbnailUrl)
                    .dontTransform()
                    .into(ivThumbnail)
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


