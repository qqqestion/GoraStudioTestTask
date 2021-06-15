package ru.tashkent.gora.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import ru.tashkent.R
import ru.tashkent.databinding.ItemPhotoBinding
import ru.tashkent.gora.data.local.PhotoView

class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<PhotoView>() {
        override fun areItemsTheSame(oldItem: PhotoView, newItem: PhotoView): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoView, newItem: PhotoView): Boolean {
            return oldItem.title == newItem.title && oldItem.url == newItem.url
        }
    }

    private val differ = AsyncListDiffer(this, callback)

    var photos: List<PhotoView>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    class ViewHolder(
        private val binding: ItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PhotoView) {
            with(binding) {
                tvTitle.text = photo.title
                ivPhoto.load(photo.url) {
                    placeholder(R.drawable.placeholder)
                    error(R.drawable.ic_baseline_report_gmailerrorred_24)
                    memoryCachePolicy(CachePolicy.ENABLED)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int {
        return photos.size
    }
}