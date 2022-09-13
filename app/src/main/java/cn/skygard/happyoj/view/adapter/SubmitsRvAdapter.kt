package cn.skygard.happyoj.view.adapter

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.*
import cn.skygard.common.base.ext.color
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ext.visible
import cn.skygard.happyoj.databinding.ItemSubmitsBinding
import cn.skygard.happyoj.domain.model.SubmitLabel
import cn.skygard.happyoj.domain.model.SubmitsItem
import java.text.SimpleDateFormat
import java.util.*

class SubmitsRvAdapter :
    ListAdapter<SubmitsItem, SubmitsRvAdapter.Holder>(SubmitsItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmitsRvAdapter.Holder {
        return Holder(ItemSubmitsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SubmitsRvAdapter.Holder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Holder(private val binding: ItemSubmitsBinding) : RecyclerView.ViewHolder(binding.root) {

        private val checkPointsRvAdapter by lazyUnlock {
            CheckpointsRvAdapter()
        }

        private fun disableSpinner() {
            binding.run {
                ivSpinner.gone()
                allCheckpoints.gone()
                cardSubmit.setOnClickListener(null)
            }
        }

        private fun enableSpinner(checkpoints: List<SubmitLabel>) {
            binding.run {
                ivSpinner.visible()
                rvCheckpoints.run {
                    adapter = checkPointsRvAdapter
                    layoutManager = GridLayoutManager(context, 5)
                }
                cardSubmit.setOnClickListener {
                    var start = 0f
                    var end = 180f
                    if (ivSpinner.rotation == 0f || ivSpinner.rotation == 360f) {
                        Log.d("SubmitsRvAdapter", "open spinner")
                        allCheckpoints.visible()
                        checkPointsRvAdapter.submitList(checkpoints)
                    } else if (ivSpinner.rotation == 180f) {
                        start = 180f
                        end = 360f
                        Log.d("SubmitsRvAdapter", "close spinner")
                        allCheckpoints.gone()
                    }
                    ObjectAnimator.ofFloat(ivSpinner, "rotation",
                        start, end).run {
                        duration = 200L
                        interpolator = OvershootInterpolator()
                        start()
                    }
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun showCheckpoints(num: Int, labels: List<SubmitLabel>) {
            assert(num == labels.size)
            binding.run {
                val checkpoints = listOf(
                    cardCheckpoint1, cardCheckpoint2,
                    cardCheckpoint3, cardCheckpoint4
                )
                checkpoints.forEachIndexed { index, it ->
                    if (index < num) {
                        it.root.visible()
                        it.tvCheckpointIndex.text = "#${index+1}/${num}"
                        it.tvCheckpointResult.text = labels[index].mess
                        it.checkpoint.setCardBackgroundColor(labels[index].color)
                    }
                    else {
                        it.root.gone()
                    }
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: SubmitsItem) {
            binding.run {
                allCheckpoints.gone()
                if (item.checkPointNum > 4) {
                    enableSpinner(item.checkPointLabels)
                } else {
                    disableSpinner()
                }
                tvScore.text = "分数: ${item.submitScore}"
                when (item.submitScore) {
                    in 60..100 -> {
                        tvScore.setTextColor(android.R.color.holo_green_dark.color)
                    }
                    in 1..59 -> {
                        tvScore.setTextColor(android.R.color.holo_orange_light.color)
                    }
                    0 -> {
                        tvScore.setTextColor(android.R.color.holo_red_dark.color)
                    }
                }
                tvTitle.text = item.submitTitle
                tvDate.text = SimpleDateFormat("MM-dd hh:mm:ss", Locale.CHINA).format(item.submitAt)
                showCheckpoints(item.checkPointNum, item.checkPointLabels)
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