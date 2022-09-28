package cn.skygard.happyoj.view.adapter

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.widget.LinearLayoutCompat
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
import kotlin.math.abs

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

                tvLastDays.text = item.deadline?.run { split(" ")[0].takeLast(5) }?:"无截止日期"
                tvStateSubmit.text = if (item.submitState == true) "状态: 已提交" else "状态: 待提交"
                tvRepo.text = "仓库: ${item.repoState?:"未提交"}"
                tvStateComment.text = if (item.commentState == true) "评测: 已评测" else "评测: 未评测"

                ivDrawer.setOnClickListener {
                    var start = listOf(0F, 25F, 1F, 0F)
                    var end = listOf(15F, 4F, 0F, 180F)
                    if (abs(layoutState.alpha - 0F) < 1e-5) {
                        val tmp = start
                        start = end
                        end = tmp
                    } else if (abs(layoutState.alpha - 1F) > 1e-5) return@setOnClickListener
                    ValueAnimator.ofFloat(start[0], end[0]).run {
                        duration = 300L
                        interpolator = LinearInterpolator()
                        addUpdateListener { anim ->
                            frameIvTask.layoutParams = LinearLayoutCompat.LayoutParams(
                                0,
                                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                                anim.animatedValue as Float
                            )
                        }
                        start()
                    }
                    ValueAnimator.ofFloat(start[1], end[1]).run {
                        duration = 300L
                        interpolator = LinearInterpolator()
                        addUpdateListener { anim ->
                            cardState.layoutParams = LinearLayoutCompat.LayoutParams(
                                0,
                                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                                anim.animatedValue as Float
                            ).apply {
                                gravity = Gravity.CENTER
                            }
                        }
                        start()
                    }
                    ValueAnimator.ofFloat(start[2], end[2]).run {
                        duration = 300L
                        interpolator = LinearInterpolator()
                        addUpdateListener { anim ->
                            layoutState.alpha = anim.animatedValue as Float
                        }
                        start()
                    }
                    ValueAnimator.ofFloat(start[3], end[3]).run {
                        duration = 300L
                        interpolator = OvershootInterpolator()
                        addUpdateListener { anim ->
                            ivDrawer.rotation = anim.animatedValue as Float
                        }
                        start()
                    }
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