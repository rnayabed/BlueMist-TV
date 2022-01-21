package com.anatame.pickaflix.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding

class HomeScreenAdapter:  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CategoryViewHolder(
        val categoryItemBinding: ItemHomeCategoryBinding
    ): RecyclerView.ViewHolder(categoryItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val categoryItemBinding = ItemHomeCategoryBinding
            .inflate(
                LayoutInflater
                .from(parent.context), parent, false)

        return CategoryViewHolder(categoryItemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as CategoryViewHolder
        holder.categoryItemBinding.apply {
            tvCategoryTitle.text = "Bruh"
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}