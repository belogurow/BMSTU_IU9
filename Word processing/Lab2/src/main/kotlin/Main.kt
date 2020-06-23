fun main(args: Array<String>) {
    val dzhekStudy = ClassLoader.getSystemResource("dzhek-study.txt").readText()
    val dzhekTest = ClassLoader.getSystemResource("dzhek-test.txt").readText()

//    Smoothing.unigram(dzhekStudy = dzhekStudy, dzhekTest = dzhekTest)

    val unigramProbabiltity = Smoothing.getProbability(studyText = dzhekStudy, testText = dzhekTest, debugOutput = true)


}
