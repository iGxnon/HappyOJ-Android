package cn.skygard.happyoj.repo.model

import android.graphics.Color
import java.util.*

data class SubmitsItem (
    val submitId: Int = -1,
    val submitUid: Int = -1,
    val submitLabel: SubmitLabel,
    val checkPointLabels: List<SubmitLabel>,
    val checkPointNum: Int = 1,
    val submitScore: Int = -1,
    val submitAt: Date
)

enum class SubmitLabel(val mess: String, val color: Int) {
    Accept("AC", android.R.color.holo_green_dark),
    RuntimeError("RE", android.R.color.holo_red_light),
    TimeLimitError("TLE", Color.YELLOW),
    WrongAnswer("WA", Color.RED),
    ComplieError("CE", Color.RED),
    MemoryLimitError("MLE", Color.YELLOW),
    OutputLimitError("OLE", Color.YELLOW)
}

