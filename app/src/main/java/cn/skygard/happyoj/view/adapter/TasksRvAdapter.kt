package cn.skygard.happyoj.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.visible
import cn.skygard.happyoj.databinding.ItemTasksBinding
import cn.skygard.happyoj.domain.model.TasksItem
import com.bumptech.glide.Glide
import java.lang.String.format

class TasksRvAdapter(private val onClick: (TasksItem, View, String, View, String) -> Unit) :
    ListAdapter<TasksItem, TasksRvAdapter.Holder>(TasksItemCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Holder(private val binding: ItemTasksBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (layoutPosition == -1)
                    return@setOnClickListener
                val item = getItem(layoutPosition)!!
                val transitionNameHeader = "header-$layoutPosition"
                binding.ivShortcut.transitionName = transitionNameHeader
                val transitionNameDesc = "desc-$layoutPosition"
                binding.tvDesc.transitionName = transitionNameDesc
                onClick(item, binding.ivShortcut, transitionNameHeader,
                    binding.tvDesc, transitionNameDesc)
            }
        }

        fun bind(item: TasksItem) {
            binding.run {
                tvTitle.text = item.title
                tvDesc.text = item.summary
                tvDate.text = format("%tF", item.date)
                if (item.imageUrl != "") {
                    ivShortcut.visible()
                    Glide.with(BaseApp.appContext)
                        .load(item.imageUrl)
                        .centerCrop()
                        .into(binding.ivShortcut)
                } else {
                    ivShortcut.gone()
                }
            }
        }
    }

    internal class TasksItemCallback : DiffUtil.ItemCallback<TasksItem>() {
        override fun areItemsTheSame(oldItem: TasksItem, newItem: TasksItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TasksItem, newItem: TasksItem): Boolean {
            return oldItem.taskId == newItem.taskId
        }

        override fun getChangePayload(oldItem: TasksItem, newItem: TasksItem): Any = ""
    }


}