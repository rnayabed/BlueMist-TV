package com.anatame.pickaflix.ui.home.adapter.rework.items

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding
import com.anatame.pickaflix.databinding.ItemHomeViewpagerBinding
import com.anatame.pickaflix.ui.home.adapter.childAdapters.MovieAdapter
import com.anatame.pickaflix.ui.home.adapter.rework.HomeItemProvider
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem


class MovieCategoryItem(
    val context: Context,
    val data: List<MovieItem>,
    val categoryTitle: String,
    val categorySubtitle: String,
    val scrollState: MutableLiveData<Int>,
): HomeItemProvider {
    private lateinit var binding: ItemHomeCategoryBinding

    inner class Holder(
        private val binding: ItemHomeCategoryBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        binding = ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun construct(position: Int, lifecycleOwner: LifecycleOwner) {
        Log.d("fromViewPagerItem", data.toString())
        setUpCategoryRV(position, lifecycleOwner)
    }

   private fun setUpCategoryRV(position: Int, lifecycleOwner: LifecycleOwner){
        binding.apply {
            val adapter = MovieAdapter(context, position)
            tvCategoryTitle.text = categoryTitle
            rvCategoryItems.adapter = adapter
            rvCategoryItems.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvCategoryItems.setHasFixedSize(true);
            rvCategoryItems.isNestedScrollingEnabled = false;

            scrollState.observe(lifecycleOwner, Observer{
                (rvCategoryItems.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(it, 200)
            })

            adapter.differ.submitList(data)
            adapter.setOnItemClickListener{pos, item, cardView ->
           //     onClick(pos, item, cardView)
                scrollState.postValue(pos)
            }
        }
    }

}