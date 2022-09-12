package cn.skygard.happyoj.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.skygard.happyoj.databinding.ItemSearchHistoryBinding

class SearchHistoryRvAdapter(val onClick: String.() -> Unit, val onDelete : (String) -> Unit) :
    RecyclerView.Adapter<SearchHistoryRvAdapter.Holder>() {

    private val mHistories : MutableList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<String>) {
        mHistories.clear()
        list.forEach {
            mHistories.add(it)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = mHistories.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        return holder.bind(mHistories[position])
    }

    inner class Holder(private val binding: ItemSearchHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layoutHistory.setOnClickListener {
                if (layoutPosition == -1)
                    return@setOnClickListener
                val item = mHistories[layoutPosition]
                item.onClick()
            }
        }

        fun bind(item: String) {
            binding.tvHistory.text = item
            binding.ivDelete.setOnClickListener {
                onDelete(mHistories[layoutPosition])
            }
        }
    }

}