import kotlin.math.pow

object Smoothing {

    fun calculate(studyText: String, testText: String) {
        // Список всех слов из тестовой выборки
        val wordsTest = testText.toLowerCase()
                .replace("-", " ")
                .split(Regex("\\P{L}+"))
                .filter { it.isNotEmpty() }
                .toList()

        println("wordsTest $wordsTest")

        // Map, где key = {Слово}, value = {сколько раз встречается это слово в тексте}
        val counterText = mutableMapOf<String, Int>()
        wordsTest.associateByTo(counterText, keySelector = { it }, valueTransform = { (counterText[it] ?: 0) + 1 })
        println(counterText)

        // P(w_i) = c_i / N
        val unigramProbability = counterText.mapValues { it.value.toFloat() / wordsTest.size }
                .toList()
                .sortedBy { (_, value) -> value }
                .asReversed()
                .toMap()

        println(unigramProbability)

        // P(w_i) = (c_i + 1) / (N + V)
        val unigramSmoothing = counterText.mapValues { (it.value.toFloat() + 1) / (wordsTest.size + counterText.size) }
                .toList()
                .sortedBy { (_, value) -> value }
                .asReversed()
                .toMap()

        println(unigramSmoothing)

        // Список биграм
        val bigrams = mutableListOf<Bigram>()
        wordsTest.forEachIndexed { index, _ ->
            if (index == wordsTest.size - 1) {
                return@forEachIndexed
            }

            bigrams.add(Bigram(wordsTest[index], wordsTest[index + 1]))
        }

        println(bigrams)

        val counterBigrams = mutableMapOf<Bigram, Int>()
        bigrams.associateByTo(counterBigrams, keySelector = { it }, valueTransform = { (counterBigrams[it] ?: 0) + 1 })

        println("counterBigrams = $counterBigrams")


        // во скольких уникальных случаях элемент встречается на первом месте в counterBigrams
        val counterForUniqueElement = { element: String ->
            counterBigrams.filterKeys { it.first == element }
                    .size
        }

        // во скольких случаях элемент встречается на первом месте в counterBigrams
        val counterForElement = { element: String ->
            counterBigrams.filter { it.key.first == element }
                    .map { it.value }
                    .sum()
        }


        val lambdas = mutableMapOf<String, Float>()
        counterBigrams.keys.forEach {
            lambdas[it.first] = 1 - counterForUniqueElement(it.first).toFloat() / (counterForUniqueElement(it.first) + counterForElement(it.first))
        }

        println("lambdas $lambdas")

        val bigramProbability = mutableMapOf<Bigram, Float>()
        counterBigrams.mapValuesTo(bigramProbability) {
            lambdas[it.key.first]!! * it.value.toFloat() / counterText[it.key.first]!!
            +(1 - lambdas[it.key.first]!!) * unigramProbability[it.key.second]!!
        }

        println("bigramProbability $bigramProbability")

    }


    fun getProbability(studyText: String, testText: String, debugOutput: Boolean = false) {
        // ------------------------------------------------------
        // Считаем сглаживаение для униграм (обычное и аддитивное)

        // 1) Список всех слов из обучающей выборки
        val studyWords = getWordsList(studyText)
        print("Слова из обучающей выборки:", studyWords, debugOutput)

        val testWords = getWordsList(testText)
        print("Слова из тестовой выборки:", testWords, debugOutput)

        val countStudyWords = studyWords.size

        // 2) Map, где key = {Слово}, value = {сколько раз встречается это слово в тексте}
        val counterStudyWords = mutableMapOf<String, Int>()
        studyWords.associateByTo(counterStudyWords, keySelector = { it }, valueTransform = {
            (counterStudyWords[it] ?: 0) + 1
        })

        val countStudyUniqueWords = counterStudyWords.size

        print("Частотность слов из обучающей выборки:", counterStudyWords, debugOutput)

        // 3) Считаем сглаживания
        val unigramProbability = calculateUnigramProbability(counterStudyWords, studyWords)
        val additiveUnigramProbability = calculateAdditiveUnigramProbability(counterStudyWords, studyWords)

        print("Вероятность униграм обучающей выборки:", unigramProbability, debugOutput)
        print("Вероятность униграм обучающей выборки с аддитивным сглаживанием:", additiveUnigramProbability, debugOutput)

        // 4) Считаем перплексию для униграм
        var product = 1.0
        testWords.forEach { word ->
            product *= if (word in studyWords) {
                additiveUnigramProbability[word]!!
            } else {
                1.0 / (countStudyUniqueWords + countStudyWords)
            }
        }

        val unigramPerplection = product.pow(-1.0 / testWords.size)
        print("Перплексия для униграм:", unigramPerplection, debugOutput = true)

        // -----------------------------------------
        // Cчитаем сглаживание по методу Witten-Bell

        // 1) Формируем список биграм
        val studyBigrams = getBigramsList(studyWords)
        print("Список биграм из обучающей выборки:", studyBigrams, debugOutput = false)

        // 2) Map, где key = {Bigram}, value = {сколько раз эта биграмма встречается в тексте}
        val counterStudyBigrams = mutableMapOf<Bigram, Int>()
        studyBigrams.associateByTo(counterStudyBigrams, keySelector = { it }, valueTransform = {
            (counterStudyBigrams[it] ?: 0) + 1
        })
        print("Частнотность биграм из обучающей выборки:", counterStudyBigrams, debugOutput)

        // 3.1) Определим лямбду-функцию, которая принимает на вход слово,
        // и возвращает сколько уникальных(!) раз это слово встречается на первом месте в списке биграм
        val uniqueElementCount =
                { element: String -> counterStudyBigrams.filterKeys { it.first == element }.size }

        // 3.3) Определим функцию, которая принимает на вход слово
        // и считает, сколько есть биграм, которые заканчиваются на данное слово
        val bigramsEndCount =
                { element: String ->
                    counterStudyBigrams.filter { it.key.second == element }
                            .map { it.value }
                            .sum()
                }

        // 4) Сформируем список лямбд для каждой биграмы
        val lambdas = mutableMapOf<Bigram, Double>()
        counterStudyBigrams.forEach { bigram, _ ->
            lambdas[bigram] = 1.0 - uniqueElementCount(bigram.first).toDouble() / (uniqueElementCount(bigram.first) + bigramsEndCount(bigram.second)).toDouble()
        }
        print("Лямбды:", lambdas, false)

        // 5) Считаем сглаживание Уиттена-Белла
        val wittenBellSmoothing = mutableMapOf<Bigram, Double>()
        counterStudyBigrams.forEach { bigram, count ->
            wittenBellSmoothing[bigram] = (lambdas[bigram]!! * count.toDouble() / counterStudyWords[bigram.first]!!
            + (1.0 - lambdas[bigram]!!) * additiveUnigramProbability[bigram.second]!!).toDouble()
        }

        val sortedWittenBellSmoothing = wittenBellSmoothing.toList()
                .sortedBy { (_, value) -> value }
                .asReversed()
                .toMap()
        print("Вероятности биграм со сглаживанием Уиттена-Белла:", sortedWittenBellSmoothing, debugOutput)

        // 6) Считаем перплексию для биграм
        val additiveBigramProbability = mutableMapOf<Bigram, Double>()
        counterStudyBigrams.mapValuesTo(additiveBigramProbability) {
            (it.value.toDouble() + 1) / (counterStudyWords[it.key.second]!! + counterStudyWords.size * counterStudyWords.size)
        }
        print("Вероятности биграм с аддитивным сглаживанием:", additiveBigramProbability, debugOutput)

        val testBigrams = getBigramsList(testWords)
        val counterTestBigrams = mutableMapOf<Bigram, Int>()
        testBigrams.associateByTo(counterTestBigrams, keySelector = { it }, valueTransform = {
            (counterTestBigrams[it] ?: 0) + 1
        })

        val studyUniqueWords = studyWords.toSet()
        product = 1.0
        testBigrams.forEach { bigram ->
            product *= when {
                bigram in studyBigrams -> wittenBellSmoothing[bigram]!!
                bigram.first in studyWords -> additiveUnigramProbability[bigram.first]!!
                else -> 1.0 / (studyUniqueWords.size * studyUniqueWords.size)
            }
        }

        val bigramPerplexity = product.pow(-1.0 / testWords.size)
        print("Перплексия для биграм:", bigramPerplexity, debugOutput = true)
    }

    private fun calculateUnigramProbability(counterStudyWords: MutableMap<String, Int>, studyWords: List<String>): Map<String, Double> {
        return counterStudyWords.mapValues { it.value.toDouble() / studyWords.size }
                .toList()
                .sortedBy { (_, value) -> value }
                .asReversed()
                .toMap()
    }

    private fun calculateAdditiveUnigramProbability(counterStudyWords: MutableMap<String, Int>, studyWords: List<String>): Map<String, Double> {
        return counterStudyWords.mapValues { (it.value.toDouble() + 1) / (studyWords.size + counterStudyWords.size) }
                .toList()
                .sortedBy { (_, value) -> value }
                .asReversed()
                .toMap()
    }

//    private fun calculateAdditiveBigramProbability()

    private fun getBigramsList(studyWords: List<String>): List<Bigram> {
        val bigrams = mutableListOf<Bigram>()
        studyWords.forEachIndexed { index, _ ->
            if (index == studyWords.size - 1) {
                return@forEachIndexed
            }

            bigrams.add(Bigram(studyWords[index], studyWords[index + 1]))
        }

        return bigrams
    }

    private fun getWordsList(text: String) = text
            .toLowerCase()
            .replace("-", " ")
            .split(Regex("\\P{L}+"))
            .filter { it.isNotEmpty() }
            .toList()

    private fun <T> print(firstText: String, obj: T, debugOutput: Boolean = false) {
        if (debugOutput) {
            println("$firstText\n\t$obj\n")
        }
    }

    private fun <T> print(firstText: String, obj: List<T>, debugOutput: Boolean = false) {
        if (debugOutput) {
            println(firstText)

            obj.forEach {
                println("\t$it")
            }
            println()
        }
    }

    private fun <T, V> print(firstText: String, obj: Map<T, V>, debugOutput: Boolean = false) {
        if (debugOutput) {
            println(firstText)

            obj.forEach {
                println("\t%10s = %3s".format(it.key, it.value))
            }
            println()
        }
    }

    data class Bigram(val first: String,
                      val second: String)
}
