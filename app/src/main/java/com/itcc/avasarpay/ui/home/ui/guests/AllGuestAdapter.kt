package com.itcc.avasarpay.ui.home.ui.guests

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itcc.avasarpay.R
import com.itcc.avasarpay.data.modal.CateListData
import com.itcc.avasarpay.data.modal.CateListRes
import com.itcc.avasarpay.data.modal.ContactData
import com.itcc.avasarpay.databinding.EventSingleBinding
import com.itcc.avasarpay.databinding.GuestSingleBinding
import com.itcc.stonna.utils.ItemClickListener
import java.util.Locale
import java.util.Random


/**
 *Created By Hardik on 06-03-2024.
 */
class AllGuestAdapter(
    private val list: ArrayList<ContactData>
) : RecyclerView.Adapter<AllGuestAdapter.DataViewHolder>() , Filterable {
    lateinit var itemClickListener: ItemClickListener<ContactData>
    var contactFilterList = ArrayList<ContactData>()

    init {
        contactFilterList = list
    }
    class DataViewHolder(private val context: Context, private val binding: GuestSingleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ContactData, itemClickListener: ItemClickListener<ContactData>) {


            binding.name.text = data.name
            binding.email.text = data.email
            binding.number.text = data.phone


            val r = Random()
            val red: Int = r.nextInt(255 - 0 + 1) + 0
            val green: Int = r.nextInt(255 - 0 + 1) + 0
            val blue: Int = r.nextInt(255 - 0 + 1) + 0

            binding.firstLetter.text = data.name?.substring(0, 1)
            binding.cardLatter.setCardBackgroundColor(Color.rgb(red, green, blue))

            binding.action.setOnClickListener {
                itemClickListener(data)
            }
            itemView.setOnClickListener {
                // itemClickListener(lead)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            parent.context,
            GuestSingleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(list[position], itemClickListener)
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                if (charSearch.isEmpty()) {
                    contactFilterList = list
                } else {
                    val resultList = ArrayList<ContactData>()
                    for (row in list) {
                        if (row.name?.lowercase(Locale.ROOT)
                                ?.contains(charSearch.lowercase(Locale.ROOT))!!
                        ) {
                            resultList.add(row)
                        }
                    }
                    contactFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = contactFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                contactFilterList = results?.values as ArrayList<ContactData>
                notifyDataSetChanged()
            }

        }
    }

}