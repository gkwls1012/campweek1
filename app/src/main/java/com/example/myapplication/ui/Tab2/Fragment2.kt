package com.example.myapplication.ui.Tab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.Fragment2Binding
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.loader.content.CursorLoader


class Fragment2 : Fragment() {

    private var _binding: Fragment2Binding? = null
    private val binding get() = _binding!!
    private lateinit var gridView: GridView
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Fragment2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        gridView = binding.gridView
        imageAdapter = ImageAdapter(
            requireContext(),
            getImages(requireContext())
        )

        gridView.adapter = imageAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private val IMAGE_PROJECTION = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATA
    )

    val MAX_IMAGES = 27
    val ORDER_BY = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    private fun getImages(context: Context): List<String> {
        val images = mutableListOf<String>()

        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursorLoader = CursorLoader(context, uri, IMAGE_PROJECTION, null, null, ORDER_BY)
        val cursor: Cursor? = cursorLoader.loadInBackground()

        cursor?.use {
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            var count = 0

            while (cursor.moveToNext() && count < MAX_IMAGES) {
                val id = cursor.getLong(idColumnIndex)
                val data = cursor.getString(dataColumnIndex)
                val contentUri: Uri = Uri.withAppendedPath(uri, id.toString())

                // Add the image path or content URI to the list
                images.add(data ?: contentUri.toString())

                count++
            }
        }

        return images
    }

}

