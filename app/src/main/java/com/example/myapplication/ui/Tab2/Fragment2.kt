package com.example.myapplication.ui.Tab2

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class Fragment2 : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private val imageList = mutableListOf<String>()
    private val selectedImages = mutableSetOf<String>()
    private val SELECT_IMAGES_REQUEST_CODE = 1
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_2, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("MyAppPreferences", Activity.MODE_PRIVATE)

        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        imageAdapter = ImageAdapter(imageList)
        recyclerView.adapter = imageAdapter

        val selectImageButton: Button = root.findViewById(R.id.btnAddImages)
        selectImageButton.setOnClickListener { selectImages() }

        // Load previously saved image URIs from shared preferences
        val savedImageList = sharedPreferences.getStringSet("imageList", emptySet())
        imageList.addAll(savedImageList ?: emptySet())
        imageAdapter.notifyDataSetChanged()

        val deletePhotoButton: Button = root.findViewById(R.id.btnDeletePhoto)
        deletePhotoButton.setOnClickListener {
            imageAdapter.deleteSelectedImages()
            selectedImages.clear()

            // Update the saved image list in shared preferences
            val editor = sharedPreferences.edit()
            editor.putStringSet("imageList", imageList.toSet())
            editor.apply()
        }

        return root
    }

    private fun selectImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Select Images"), SELECT_IMAGES_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_IMAGES_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val clipData = data?.clipData

            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    imageList.add(imageUri.toString())
                }
            } else {
                val imageUri = data?.data
                imageList.add(imageUri.toString())
            }

            // Update the saved image list in shared preferences
            val editor = sharedPreferences.edit()
            editor.putStringSet("imageList", imageList.toSet())
            editor.apply()

            imageAdapter.notifyDataSetChanged()
        }
    }
}
