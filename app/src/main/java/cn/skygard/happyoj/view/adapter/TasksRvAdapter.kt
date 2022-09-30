package cn.skygard.happyoj.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.visible
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ItemTasksBinding
import cn.skygard.happyoj.repo.remote.model.Task
import com.bumptech.glide.Glide

class TasksRvAdapter(private val onClick: (Task.TaskSubject) -> Unit) :
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
                onClick(item)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Task.TaskSubject) {
            binding.run {
                tvTitle.text = item.title
                tvDesc.text = item.summary
                tvUpdateTime.text = item.updateTime.split(" ")[0]
                if (item.imageUrl != "") {
                    ivTask.visible()
                    Glide.with(BaseApp.appContext)
                        .load(item.imageUrl)
                        .error(R.drawable.err_avatar)
                        .centerCrop()
                        .into(ivTask)
                } else {
                    ivTask.gone()
                }
                tvLastDays.gone()
                cardState.gone()
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