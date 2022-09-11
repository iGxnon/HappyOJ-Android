package cn.skygard.happyoj.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.skygard.happyoj.databinding.ItemSearchHistoryBinding

class SearchHistoryRvAdapter(val onClick: String.() -> Unit) :
    ListAdapter<String, SearchHistoryRvAdapter.Holder>(SearchHistoryCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class Holder(private val binding: ItemSearchHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (layoutPosition == -1)
                    return@setOnClickListener
                val item = getItem(layoutPosition)!!
                item.onClick()
            }
        }

        fun bind(item: String) {
            binding.tvHistory.text = item
        }
    }

    internal class SearchHistoryCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: String, newItem: String): Any = ""
    }


}