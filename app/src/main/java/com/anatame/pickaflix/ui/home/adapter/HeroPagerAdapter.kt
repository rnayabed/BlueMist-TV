package com.anatame.pickaflix.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import android.content.Context
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.anatame.pickaflix.databinding.ItemHomeViewpagerItemBinding
import com.bumptech.glide.Glide


class HeroPagerAdapter(
    val context: Context
) : RecyclerView.Adapter<HeroPagerAdapter.HeroPagerViewHolder>() {

    inner class HeroPagerViewHolder(val binding: ItemHomeViewpagerItemBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<HeroItem>() {
        override fun areItemsTheSame(oldItem: HeroItem, newItem: HeroItem): Boolean {
            return oldItem.Url == newItem.Url
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

    private var onItemClickListener: ((Int, HeroItem, CardView) -> Unit)? = null

    override fun onBindViewHolder(holder: HeroPagerViewHolder, position: Int) {
        Log.d("viewPager", "holder")
        val heroItem = differ.currentList[position]

        holder.itemView.apply {
            holder.binding.apply {
                Glide.with(context)
                    .load(heroItem.thumbnailUrl)
                    .dontTransform()
                    .into(ivHeroThumbnail)

                ViewCompat.setTransitionName(
                    heroCardContainer,
                    "iv$position ${heroItem.Url}"
                )
            }
            setOnClickListener {view ->
                onItemClickListener?.let { it(position, heroItem, holder.binding.heroCardContainer) }
            }
        }

    }

    fun setOnItemClickListener(listener: (Int, HeroItem, CardView) -> Unit) {
        onItemClickListener = listener
    }


}
