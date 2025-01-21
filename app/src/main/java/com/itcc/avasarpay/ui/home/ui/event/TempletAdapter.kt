package com.itcc.avasarpay.ui.home.ui.event

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itcc.avasarpay.R
import com.itcc.avasarpay.data.modal.CategoryItem
import com.itcc.avasarpay.data.modal.GuestIteam
import com.itcc.avasarpay.data.modal.TemplatesItem
import com.itcc.avasarpay.databinding.RowGuestBinding
import com.itcc.avasarpay.databinding.RowTemplateSingleBinding
import com.itcc.stonna.utils.ItemClickListener
import java.util.Random


/**
 *Created By Hardik on 06-03-2024.
 */
class TempletAdapter(
    private val list: List<TemplatesItem>,
    private val onItemSelected: (TemplatesItem,Int) -> Unit
) : RecyclerView.Adapter<TempletAdapter.DataViewHolder>()  {
    lateinit var itemClickListener: ItemClickListener<TemplatesItem>

    class DataViewHolder(private val context: Context, private val binding: RowTemplateSingleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TemplatesItem, onItemSelected: (TemplatesItem, Int) -> Unit) {
            binding.rbchoose.isChecked = data.isSelected
            Glide.with(context).load(data.thumbnail).placeholder(R.drawable.placeholder1)
                .into(binding.tempImg)

            itemView.setOnClickListener {
                onItemSelected(data, absoluteAdapterPosition)
            }

            binding.rbchoose.setOnClickListener{
                onItemSelected(data, absoluteAdapterPosition)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            parent.context,
            RowTemplateSingleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(list[position], onItemSelected)


}