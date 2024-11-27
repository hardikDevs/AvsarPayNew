package com.itcc.avasarpay.ui.home.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itcc.avasarpay.R
import com.itcc.avasarpay.data.modal.CategoryItem
import com.itcc.avasarpay.databinding.EventSingleBinding
import com.itcc.stonna.utils.ItemClickListener


/**
 *Created By Hardik on 06-03-2024.
 */
class CategoryAdapter(
    private val list: List<CategoryItem>
) : RecyclerView.Adapter<CategoryAdapter.DataViewHolder>() {
    lateinit var itemClickListener: ItemClickListener<CategoryItem>

    class DataViewHolder(private val context: Context,private val binding: EventSingleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CategoryItem, itemClickListener: ItemClickListener<CategoryItem>) {
            binding.tvCategoryName.text = data.name

            Glide.with(context).load(data.featuredImage).placeholder(R.drawable.placeholder1)
                .into(binding.imgDisable)
            itemView.setOnClickListener {
               // itemClickListener(lead)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            parent.context,
            EventSingleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(list[position], itemClickListener)


}