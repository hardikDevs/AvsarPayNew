package com.itcc.avasarpay.ui.home.ui.event

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itcc.avasarpay.R
import com.itcc.avasarpay.data.modal.CategoryItem
import com.itcc.avasarpay.databinding.EventSingleBinding
import com.itcc.avasarpay.databinding.RowCategorySelectionBinding
import com.itcc.stonna.utils.ItemClickListener


/**
 *Created By Hardik on 06-03-2024.
 */
class CategorySelectionAdapter(
    private val list: List<CategoryItem>,
    private val onItemSelected: (CategoryItem,Int) -> Unit
) : RecyclerView.Adapter<CategorySelectionAdapter.DataViewHolder>() {


    class DataViewHolder(private val context: Context,private val binding: RowCategorySelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CategoryItem, onItemSelected: (CategoryItem, Int) -> Unit) {
            binding.rb.text = data.name
            binding.rb.isChecked = data.isSelected

            // Set button tint dynamically for older devices
            val colorStateList = ContextCompat.getColorStateList(itemView.context, R.color.selected_radio_button)
            CompoundButtonCompat.setButtonTintList(binding.rb, colorStateList)

            // Change background based on selection
            if (data.isSelected) {
              binding.itemLayout.setBackgroundResource(R.drawable.category_selected)
                binding.rb.setTextColor(context.resources.getColor(R.color.white, context.resources.newTheme()))
            } else {
                binding.itemLayout.setBackgroundResource(R.drawable.category_defualt)
                binding.rb.setTextColor(context.resources.getColor(R.color.black,context.resources.newTheme()))
            }
            itemView.setOnClickListener {
                onItemSelected(data, absoluteAdapterPosition)
            }

            binding.rb.setOnClickListener{
                onItemSelected(data, absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            parent.context,
            RowCategorySelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(list[position], onItemSelected)


}