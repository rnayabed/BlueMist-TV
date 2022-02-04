package com.anatame.pickaflix.ui.detail.adapter.childAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.databinding.ItemDetailEpisodeBinding
import com.anatame.pickaflix.ui.detail.models.EpisodeItem


class EpisodeRVAdapter (
    val episodeList: List<EpisodeItem>
)  : RecyclerView.Adapter<EpisodeRVAdapter.EpisodeViewHolder>() {
    inner class EpisodeViewHolder(val binding: ItemDetailEpisodeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemDetailEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return EpisodeViewHolder(binding)
    }

    private var onItemClickListener: ((Int, String) -> Unit) ? = null

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.binding.apply {
            epsBtn.text = episodeList.get(position).title
            epsBtn.setOnClickListener{
                onItemClickListener?.let { it(position, episodeList.get(position).episodeDataID) }
            }
        }

    }
    fun setOnItemClickListener(listener: (Int, String) -> Unit) {
        onItemClickListener = listener
    }


    override fun getItemCount(): Int {
        return episodeList.size
    }

}