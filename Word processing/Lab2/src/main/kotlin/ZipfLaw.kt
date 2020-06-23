object ZipfLaw {

    data class Zipf(val word: String,
                    val counter: Int,
                    val rank: Int,
                    val result: Int)

    fun processText(text: String) : List<Zipf> {
        val counterText = mutableMapOf<String, Int>()
        val zipfList = mutableListOf<Zipf>()
        var rank = 0

        text.toLowerCase()
                .split(Regex("\\P{L}+"))
                .associateByTo(counterText, keySelector = {it}, valueTransform = {(counterText[it] ?: 0) + 1})
                .toList()
                .sortedBy { (_, value) -> value }
                .asReversed()
                .take(1000)
                .toMap()
                .forEach{ k, v ->
                    zipfList.add(Zipf(k, v, ++rank, v * rank))
                }

        return zipfList
    }
}