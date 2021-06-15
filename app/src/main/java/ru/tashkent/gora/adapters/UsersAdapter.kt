package ru.tashkent.gora.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.tashkent.databinding.ItemUserBinding
import ru.tashkent.gora.data.local.UserView

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<UserView>() {
        override fun areItemsTheSame(oldItem: UserView, newItem: UserView): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserView, newItem: UserView): Boolean {
            return oldItem.name == newItem.name
        }
    }

    private val differ = AsyncListDiffer(this, callback)

    var users: List<UserView>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private var onUserClickListener: ((UserView) -> Unit)? = null

    fun setOnUserClickListener(listener: (UserView) -> Unit) {
        onUserClickListener = listener
    }

    class ViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserView) {
            with(binding) {
                tvName.text = user.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onUserClickListener?.let { click ->
                click(user)
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}