
package com.example.gomommy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TipsItemAdapter(private var tipsItemList: ArrayList<tipsItem>) : RecyclerView.Adapter<TipsItemAdapter.TipsItemViewHolder>() {

    //Define a ViewHolder to hold the views for each item in the RecyclerView
    class TipsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Declare the views in your item layout
        val tipsItemTitleTextView: TextView = itemView.findViewById(R.id.tipsItemTitleTextView)
        val tipsItemDescTextView: TextView = itemView.findViewById(R.id.tipsItemDescTextView)
        val tipsItemSourceTextView: TextView = itemView.findViewById(R.id.tipsItemSourceTextView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsItemViewHolder {

        //Inflate the item layout XML and return a new tipsItemViewHolder instance

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_tips_and_advice_item, parent, false)
        return TipsItemViewHolder(itemView)

    }

    override fun getItemCount(): Int {

        // return the number of items in the list
        return tipsItemList.size

    }

    override fun onBindViewHolder(holder: TipsItemViewHolder, position: Int) {

        // Get the item at the current position
        val tipsItem = tipsItemList[position]

        // Bind the item data to the views in the item layout
        holder.tipsItemTitleTextView.text = tipsItem.itemTitle
        holder.tipsItemDescTextView.text = tipsItem.itemDesc
        holder.tipsItemSourceTextView.text = tipsItem.itemSource


    }

    /*fun filterList(filteredList: ArrayList<tipsItem>) {
        tipsItemList = filteredList
        notifyDataSetChanged()
    }*/
}