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
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.adapter.BaseItemCallback
import cn.skygard.common.base.adapter.BasePagingAdapter
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.visible
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ItemTasksBinding
import cn.skygard.happyoj.repo.remote.model.Task
import cn.skygard.happyoj.view.activity.CommentActivity
import com.bumptech.glide.Glide
import kotlin.math.abs

class TasksPagingAdapter(private val onClick: (Task.TaskSubject) -> Unit, val onClickCard: (Long, String) -> Unit) :
    BasePagingAdapter<ItemTasksBinding, Task.TaskSubject>(
    BaseItemCallback { a, b ->
        return@BaseItemCallback a.id == b.id
    }
) {

    override val holderInit: Holder<ItemTasksBinding>.() -> Unit
        get() = {
            binding.layoutContent.setOnClickListener {
                if (layoutPosition == -1)
                    return@setOnClickListener
                val item = getItem(layoutPosition)!!
                onClick(item)
            }
            binding.cardState.setOnClickListener {
                if (layoutPosition == -1)
                    return@setOnClickListener
                val item = getItem(layoutPosition)!!
                onClickCard(item.id, item.title)
            }
        }

    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemTasksBinding {
        return ItemTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder<ItemTasksBinding>, position: Int) {
        val item = getItem(position)!!
        holder.binding.run {
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