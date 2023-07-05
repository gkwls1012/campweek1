package com.example.myapplication.ui.Tab2

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R

class ImageAdapter(private val imageList: MutableList<String>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val selectedPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUri = Uri.parse(imageList[position])
        Glide.with(holder.itemView)
            .load(imageUri)
            .into(holder.imageView)

        val isItemSelected = selectedPositions.contains(position)
        holder.itemView.isSelected = isItemSelected
        holder.imageView.alpha = if (isItemSelected) 0.5f else 1.0f
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun deleteSelectedImages() {
        val sortedPositions = selectedPositions.toList().sortedDescending()
        for (position in sortedPositions) {
            imageList.removeAt(position)
        }
        selectedPositions.clear()
        notifyDataSetChanged()
    }

    fun toggleSelection(position: Int) {
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(position)
        } else {
            selectedPositions.add(position)
        }
        notifyItemChanged(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    toggleSelection(position)
                }
            }
        }
    }
}