package cn.skygard.happyoj.view.fragment

import java.util.regex.Pattern

internal object Utils {

    fun checkPwd(pwd: String): Boolean {
        val pattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9])(.{6,32})\$"
        return Pattern.compile(pattern).matcher(pwd).matches()
    }

    fun checkEmail(email: String): Boolean {
        val pattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
        return Pattern.compile(pattern).matcher(email).matches()
    }

    fun getPwdError(pwd: String): String {
        val err = mutableListOf<String>()
        val errBuf = StringBuilder()
        if (pwd.length < 6 || pwd.length > 32)
            err.add("长度6-32")
        if (pwd.lowercase() == pwd || pwd.uppercase() == pwd)
            err.add("大小写字符")
        if (!Pattern.compile("[@!$^&*+_.:=|{}';,<>/?~]").matcher(pwd).find())
            err.add("特殊字符")
        if (!Pattern.compile("[0-9]").matcher(pwd).find())
            err.add("数字")
        err.joinTo(errBuf, separator = ",")
        return errBuf.toString()
    }

}