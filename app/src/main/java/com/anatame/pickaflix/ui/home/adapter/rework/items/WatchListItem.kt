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
import com.anatame.pickaflix.ui.home.HomeFragment
import com.anatame.pickaflix.ui.home.adapter.rework.childAdapters.MovieAdapter
import com.anatame.pickaflix.ui.home.adapter.rework.childAdapters.WatchListAdapter
import com.anatame.pickaflix.ui.home.adapter.rework.providers.HomeItemProvider
import com.anatame.pickaflix.utils.data.db.entities.Movie
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.utils.dp
import com.anatame.pickaflix.utils.px


class WatchListItem(
    val data: List<Movie>,
    val categoryTitle: String,
    val categorySubtitle: String,
    val scrollState: MutableLiveData<Int>,
): HomeItemProvider {
    private lateinit var binding: ItemHomeCategoryBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var holder: Holder

    inner class Holder(
        private val binding: ItemHomeCategoryBinding
    ): RecyclerView.ViewHolder(binding.root){

    }

    override fun getViewHolder(context: Context, parent: ViewGroup, lifecycleOwner: LifecycleOwner, homeFragment: HomeFragment): RecyclerView.ViewHolder {
        binding = ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        holder = Holder(binding)
        val position = holder.absoluteAdapterPosition
        this.homeFragment = homeFragment

        setUpCategoryRV(context, position, lifecycleOwner)

        return holder
    }

    override fun onBindHolder(context: Context, position: Int, lifecycleOwner: LifecycleOwner) {
        Log.d("fromViewPagerItem", data.toString())
    }

   private fun setUpCategoryRV(context: Context, position: Int, lifecycleOwner: LifecycleOwner){
        binding.apply {
            val adapter = WatchListAdapter(context, position)
            tvCategoryTitle.text = categoryTitle

            val params: ViewGroup.LayoutParams = rvCategoryItems.layoutParams
            params.height = 130.px
            rvCategoryItems.layoutParams = params

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

                homeFragment.navigateToDetailFromWatchList(cardView,
                    holder,
                    item)
                scrollState.postValue(pos)
            }
        }
    }

}