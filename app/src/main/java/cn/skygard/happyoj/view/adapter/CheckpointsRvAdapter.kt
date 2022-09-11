package cn.skygard.happyoj.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.skygard.happyoj.databinding.ItemCheckpointBinding
import cn.skygard.happyoj.repo.model.SubmitLabel

class CheckpointsRvAdapter :
    ListAdapter<SubmitLabel, CheckpointsRvAdapter.Holder>(CheckpointsCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemCheckpointBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position, getItem(position))
    }

    inner class Holder(private val binding: ItemCheckpointBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(index: Int, item: SubmitLabel) {
            binding.item.run {
                tvCheckpointIndex.text = "#${index+1}/${itemCount}"
                tvCheckpointResult.text = item.mess
                checkpoint.setCardBackgroundColor(item.color)
            }
        }
    }


    internal class CheckpointsCallback : DiffUtil.ItemCallback<SubmitLabel>() {
        override fun areItemsTheSame(oldItem: SubmitLabel, newItem: SubmitLabel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SubmitLabel, newItem: SubmitLabel): Boolean {
            return oldItem.mess == newItem.mess
        }

        override fun getChangePayload(oldItem: SubmitLabel, newItem: SubmitLabel): Any = ""
    }


}