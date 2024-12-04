import java.io.Reader

class StringProducer(private val input: Reader) {
    private var curChar: Char? = readNext()
    private var curPos = 0
    private var markedPos = 0
    private var markedChar: Char? = null
    private var activeTransaction = false
        set(newVal) {
            if (!field && newVal) {
                input.mark(MAX_TOKEN_LENGTH)
                markedChar = curChar
                markedPos = curPos
            }
            if (field && !newVal) {
                input.reset()
                curChar = markedChar
                curPos = markedPos
            }
            field = newVal
        }

    private fun readNext(): Char? {
        val char = input.read()
        if (char < 0) {
            return null
        }
        return char.toChar()
    }

    fun commit() {
        input.mark(0)
        markedChar = curChar
        markedPos = curPos
        activeTransaction = false
    }

    fun beginTransaction() {
        activeTransaction = true
    }

    fun cancel() {
        activeTransaction = false
    }

    fun peek(): Char? = curChar

    fun pos(): Int = curPos

    fun take() {
        curChar = readNext()
        curPos++
    }

    companion object {
        const val MAX_TOKEN_LENGTH = 64
    }
}

fun StringProducer.expect(string: String): Boolean {
    if (peek() != string.firstOrNull()) return false

    return transaction {
        for (i in string) {
            if (peek() != i) {
                return@transaction null
            }
            take()
        }
        return@transaction true
    } != null
}

fun <T> StringProducer.transaction(body: StringProducer.() -> T?): T? {
    beginTransaction()
    val result: T? = body()
    if (result != null) {
        commit()
    } else {
        cancel()
    }
    return result
}
