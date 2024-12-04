

suspend fun main() {
    val input = System.`in`.bufferedReader()
    val parser = Parser(input)
    val parsed = parser.parse()

    val renderer = TreePrinter(System.`out`.bufferedWriter())
    renderer.print(parsed)
}
