package cn.skygard.happyoj.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.skygard.happyoj.databinding.ItemSubmitsBinding
import cn.skygard.happyoj.repo.model.SubmitsItem

class SubmitsRvAdapter(private val onClick: SubmitsItem.() -> Unit) :
    ListAdapter<SubmitsItem, SubmitsRvAdapter.Holder>(SubmitsItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmitsRvAdapter.Holder {
        return Holder(ItemSubmitsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SubmitsRvAdapter.Holder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Holder(private val binding: ItemSubmitsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (layoutPosition == -1)
                    return@setOnClickListener
                val item = getItem(layoutPosition)!!
                item.onClick()
            }
        }

        fun bind(item: SubmitsItem) {
            binding.run {

            }
        }
    }

    internal class SubmitsItemCallback : DiffUtil.ItemCallback<SubmitsItem>() {
        override fun areItemsTheSame(oldItem: SubmitsItem, newItem: SubmitsItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SubmitsItem, newItem: SubmitsItem): Boolean {
            return oldItem.submitId == newItem.submitId
        }

        override fun getChangePayload(oldItem: SubmitsItem, newItem: SubmitsItem): Any = ""
    }

}