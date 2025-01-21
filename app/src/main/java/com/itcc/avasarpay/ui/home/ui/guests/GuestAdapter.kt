package com.itcc.avasarpay.ui.home.ui.guests

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itcc.avasarpay.data.modal.GuestIteam
import com.itcc.avasarpay.databinding.RowGuestBinding
import com.itcc.stonna.utils.ItemClickListener
import java.util.Random


/**
 *Created By Hardik on 06-03-2024.
 */
class GuestAdapter(
    private val list: List<GuestIteam>
) : RecyclerView.Adapter<GuestAdapter.DataViewHolder>()  {
    lateinit var itemClickListener: ItemClickListener<GuestIteam>

    class DataViewHolder(private val context: Context, private val binding: RowGuestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GuestIteam, itemClickListener: ItemClickListener<GuestIteam>) {


            binding.name.text = data.name
            binding.email.text = data.email
            binding.number.text = data.phone


            val r = Random()
            val red: Int = r.nextInt(255 - 0 + 1) + 0
            val green: Int = r.nextInt(255 - 0 + 1) + 0
            val blue: Int = r.nextInt(255 - 0 + 1) + 0

            binding.firstLetter.text = data.name?.substring(0, 1)
            binding.cardLatter.setCardBackgroundColor(Color.rgb(red, green, blue))

            /*binding.action.setOnClickListener {
                itemClickListener(data)
            }*/
            itemView.setOnClickListener {
                // itemClickListener(lead)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            parent.context,
            RowGuestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(list[position], itemClickListener)


}