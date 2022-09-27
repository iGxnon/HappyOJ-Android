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
import cn.skygard.happyoj.repo.remote.model.Task
import com.bumptech.glide.Glide

class TasksRvAdapter(private val onClick: (Task.TaskSubject, View, String, View, String) -> Unit) :
    ListAdapter<Task.TaskSubject, TasksRvAdapter.Holder>(TasksItemCallback()){

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

        fun bind(item: Task.TaskSubject) {
            binding.run {
                tvTitle.text = item.title
                tvDesc.text = item.summary
                tvDate.text = item.updateTime.split(" ")[0]
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

    internal class TasksItemCallback : DiffUtil.ItemCallback<Task.TaskSubject>() {
        override fun areItemsTheSame(oldItem: Task.TaskSubject, newItem: Task.TaskSubject): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task.TaskSubject, newItem: Task.TaskSubject): Boolean {
            return oldItem.id == newItem.id
        }

        override fun getChangePayload(oldItem: Task.TaskSubject, newItem: Task.TaskSubject): Any = ""
    }


}