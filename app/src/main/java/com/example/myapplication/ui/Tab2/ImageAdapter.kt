package com.example.myapplication.ui.Tab2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myapplication.R

class ImageAdapter(private val context: Context, private val images: List<String>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(position: Int): Any {
        return images[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView

        if (convertView == null) {
            // Create a new ImageView if convertView is null
            imageView = ImageView(context)
            imageView.layoutParams = AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            // Reuse convertView if not null
            imageView = convertView as ImageView
        }

        // Load the image into the ImageView using Glide
        Glide.with(context)
            .load(images[position])
            .into(imageView)

        return imageView
    }
}

