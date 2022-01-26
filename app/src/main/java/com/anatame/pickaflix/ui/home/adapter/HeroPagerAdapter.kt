package com.anatame.pickaflix.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.HeroItem
import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.anatame.pickaflix.databinding.ItemHomeViewpagerItemBinding


class HeroPagerAdapter(
    val context: Context
) : RecyclerView.Adapter<HeroPagerAdapter.HeroPagerViewHolder>() {

    inner class HeroPagerViewHolder(val binding: ItemHomeViewpagerItemBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<HeroItem>() {
        override fun areItemsTheSame(oldItem: HeroItem, newItem: HeroItem): Boolean {
            return oldItem.source == newItem.source
        }

        override fun areContentsTheSame(oldItem: HeroItem, newItem: HeroItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroPagerViewHolder {
        val binding = ItemHomeViewpagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HeroPagerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Int, HeroItem, ImageView) -> Unit)? = null

    override fun onBindViewHolder(holder: HeroPagerViewHolder, position: Int) {
        Log.d("viewPager", "holder")
        val heroItem = differ.currentList[position]
    //    ViewCompat.setTransitionName(holder.binding.ivMovieThumnail, "iv$position")

        holder.itemView.apply {
            holder.binding.apply {
                 tvId.text = heroItem.title
            }
            setOnClickListener {view ->
              //  onItemClickListener?.let { it(position, heroItem, holder.binding.ivHero) }
            }
        }

    }

    fun setOnItemClickListener(listener: (Int, HeroItem, ImageView) -> Unit) {
        onItemClickListener = listener
    }


//    fun setOnItemClickListener(listener: (MovieItem, ImageView) -> Unit) {
//        onItemClickListener = listener
//    }

}
