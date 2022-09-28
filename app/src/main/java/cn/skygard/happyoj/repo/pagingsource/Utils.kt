package cn.skygard.happyoj.repo.pagingsource

internal object Utils {
    fun parseRepoUrl(url: String) : List<String> {
        return url
            .replace("https://", "")
            .replace("http://", "")
            .split("/")
            .filterNot {
                it.contains("github.com")
            }.filterNot {
                it.isEmpty()
            }
    }
}