package cn.skygard.happyoj.repo.model

import cn.skygard.common.base.ext.color
import java.util.*

data class SubmitsItem (
    val submitId: Int = -1,
    val submitUid: Int = -1,
    val submitTitle: String,
    val checkPointLabels: List<SubmitLabel>,
    val checkPointNum: Int = 1,
    val submitScore: Int = -1,
    val submitAt: Date
)

enum class SubmitLabel(val mess: String, val color: Int) {
    Accept("AC", android.R.color.holo_green_dark.color),
    RuntimeError("RE", android.R.color.holo_red_dark.color),
    TimeLimitError("TLE", android.R.color.holo_orange_light.color),
    WrongAnswer("WA", android.R.color.holo_red_dark.color),
    ComplieError("CE", android.R.color.holo_red_dark.color),
    MemoryLimitError("MLE", android.R.color.holo_orange_light.color),
    OutputLimitError("OLE", android.R.color.holo_orange_light.color)
}

