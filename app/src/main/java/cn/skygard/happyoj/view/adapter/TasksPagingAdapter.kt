package cn.skygard.happyoj.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.adapter.BaseItemCallback
import cn.skygard.common.base.adapter.BasePagingAdapter
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.visible
import cn.skygard.happyoj.databinding.ItemTasksBinding
import cn.skygard.happyoj.repo.remote.model.Task
import com.bumptech.glide.Glide

class TasksPagingAdapter(private val onClick: (Task.TaskSubject) -> Unit) :
    BasePagingAdapter<ItemTasksBinding, Task.TaskSubject>(
    BaseItemCallback { a, b ->
        return@BaseItemCallback a.id == b.id
    }
) {

    override val holderInit: Holder<ItemTasksBinding>.() -> Unit
        get() = {
            this.binding.root.setOnClickListener {
                if (layoutPosition == -1)
                    return@setOnClickListener
                val item = getItem(layoutPosition)!!
                onClick(item)
            }
        }

    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemTasksBinding {
        return ItemTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder: Holder<ItemTasksBinding>, position: Int) {
        val item = getItem(position)!!
        holder.binding.run {
            tvTitle.text = item.title
            tvDesc.text = item.summary
            tvDate.text = item.updateTime.split(" ")[0]
            if (item.imageUrl != "") {
                ivShortcut.visible()
                Glide.with(BaseApp.appContext)
                    .load(item.imageUrl)
                    .centerCrop()
                    .into(ivShortcut)
            } else {
                ivShortcut.gone()
            }
        }
    }

}