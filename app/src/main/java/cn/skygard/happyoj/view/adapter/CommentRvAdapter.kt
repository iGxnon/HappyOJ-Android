package cn.skygard.happyoj.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.skygard.happyoj.databinding.ItemCommentBinding
import cn.skygard.happyoj.repo.remote.model.Comments

class CommentRvAdapter :
    ListAdapter<Comments.CommitComment, CommentRvAdapter.Holder>(CommentCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Holder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Comments.CommitComment) {
            binding.run {
                tvTutor.text = "导师: ${item.tutorName}"
                tvComment.text = "附言: ${item.comment}"
                tvScore.text = "分数: ${item.score} 分"
                tvTime.text = item.updateTime
            }
        }
    }

    internal class CommentCallback : DiffUtil.ItemCallback<Comments.CommitComment>() {
        override fun areItemsTheSame(oldItem: Comments.CommitComment, newItem: Comments.CommitComment): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Comments.CommitComment, newItem: Comments.CommitComment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun getChangePayload(oldItem: Comments.CommitComment, newItem: Comments.CommitComment): Any = ""
    }

}