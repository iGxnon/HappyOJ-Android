package cn.skygard.happyoj.view.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.adapter.BaseItemCallback
import cn.skygard.common.base.adapter.BasePagingAdapter
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ItemLabCommitBinding
import cn.skygard.happyoj.repo.remote.model.RepoCommit
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class RepoCommitPagingAdapter :
    BasePagingAdapter<ItemLabCommitBinding, RepoCommit>(
        BaseItemCallback { a, b ->
            return@BaseItemCallback a.sha == b.sha
        }
    ) {

    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemLabCommitBinding {
        return ItemLabCommitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    // @author 寒雨
    private fun getNewsTimeStr(date: Date): String {
        val subTime = System.currentTimeMillis() - date.time
        val MILLIS_LIMIT = 1000.0
        val SECONDS_LIMIT = 60 * MILLIS_LIMIT
        val MINUTES_LIMIT = 60 * SECONDS_LIMIT
        val HOURS_LIMIT = 24 * MINUTES_LIMIT
        val DAYS_LIMIT = 30 * HOURS_LIMIT
        return if (subTime < MILLIS_LIMIT) {
            "刚刚"
        } else if (subTime < SECONDS_LIMIT) {
            (subTime / MILLIS_LIMIT).roundToInt()
                .toString() + " " + "秒前"
        } else if (subTime < MINUTES_LIMIT) {
            (subTime / SECONDS_LIMIT).roundToInt()
                .toString() + " " + "分钟前"
        } else if (subTime < HOURS_LIMIT) {
            (subTime / MINUTES_LIMIT).roundToInt().toString() + " " + "小时前"
        } else if (subTime < DAYS_LIMIT) {
            (subTime / HOURS_LIMIT).roundToInt().toString() + " " + "天前"
        } else date.asString()
    }

    private fun Date.asString(format: String = "yyyy-MM-dd"): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(this)
    }

    override fun onBindViewHolder(holder: Holder<ItemLabCommitBinding>, position: Int) {
        val item = getItem(position)!!
        holder.binding.run {
            tvLabel.text = item.commit.message.split("\n")[0]
            tvCommitter.text = item.commit.committer.name
            Glide.with(BaseApp.appContext)
                .load(item.committer.avatarUrl)
                .apply(RequestOptions().error(R.drawable.err_avatar))
                .into(sivCommitter)
            tvTime.text = getNewsTimeStr(item.commit.committer.date)
            val sha = item.sha.take(10)
            tvCommitId.text = sha
            ivCopy.setOnClickListener {
                (BaseApp.appContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                    .setPrimaryClip(ClipData.newPlainText("SHA", item.sha))
                Toast.makeText(BaseApp.appContext, "复制成功", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}