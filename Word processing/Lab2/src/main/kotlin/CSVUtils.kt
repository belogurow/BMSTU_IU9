import java.io.FileWriter

object CSVUtils {

    fun writeZipfList(zipfList: List<ZipfLaw.Zipf>, fileName: String) {
        FileWriter("$fileName.csv").use { out ->
            zipfList.forEach {
                out.write("${it.rank},${it.counter},${it.result},${it.word},\n")
            }
        }
    }
}