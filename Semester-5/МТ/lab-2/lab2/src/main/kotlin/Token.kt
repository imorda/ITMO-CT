import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.ParseException

fun newLexer(producer: StringProducer): Flow<Token> {
    return flow {
        do {
            val parsed: Token? = Token.parse(producer)
            parsed?.let {
//                println(it)
                emit(it)
            }
        } while (parsed != null)
        if (producer.peek() != null) throw ParseException("Syntax error", producer.pos())
    }
}

sealed interface Token {
    companion object {
        fun parse(producer: StringProducer): Token? = listOf(
            Token::spaceConsumer,
            Keyword::parse,
            Number::parse,
            Word::parse,
        ).firstNotNullOfOrNull { it(producer) }

        private fun spaceConsumer(producer: StringProducer): Token? {
            while (Character.isWhitespace(producer.peek() ?: '$')) {
                producer.take()
            }
            return null
        }
    }

    val label: String

    data class Word(val value: String) : Token {
        companion object {
            fun parse(producer: StringProducer): Token? = producer.transaction {
                val result = StringBuilder()
                var char = producer.peek()
                if (char != null && Character.isLetter(char)) {
                    result.append(char)
                    producer.take()
                    char = producer.peek()
                    while (char != null && Character.isLetterOrDigit(char)) {
                        result.append(char)
                        producer.take()
                        char = producer.peek()
                    }
                }
                if (result.isNotEmpty()) { return@transaction Word(result.toString()) }
                return@transaction null
            }
        }

        override val label: String
            get() = value
    }

    data class Number(val value: Int) : Token {
        companion object {
            fun parse(producer: StringProducer): Token? = producer.transaction {
                val result = StringBuilder()
                if (producer.peek() == '-') {
                    result.append('-')
                    producer.take()
                }
                while (true) {
                    val digit = producer.peek()
                    if (!Character.isDigit(digit ?: '$')) { break }
                    result.append(digit)
                    producer.take()
                }
                val parsed = result.toString().toIntOrNull()
                if (parsed != null) {
                    return@transaction Number(parsed)
                }
                return@transaction null
            }
        }

        override val label: String
            get() = value.toString()
    }

    sealed class Keyword : Token {
        companion object {
            fun parse(producer: StringProducer): Token? = Keyword::class.sealedSubclasses
                .sortedByDescending { it.objectInstance!!.label.length }.firstNotNullOfOrNull {
                    it.objectInstance?.let { instance ->
                        if (producer.expect(instance.label)) { return@firstNotNullOfOrNull instance }
                        return@firstNotNullOfOrNull null
                    }
                }
        }

        data object Int : Keyword() { override val label: String get() = "int" }
        data object Char : Keyword() { override val label: String get() = "char" }
        data object Long : Keyword() { override val label: String get() = "long" }

        data object LessEquals : Keyword() { override val label: String get() = "<=" }
        data object GreaterEquals : Keyword() { override val label: String get() = ">=" }
        data object LessThan : Keyword() { override val label: String get() = "<" }
        data object GreaterThan : Keyword() { override val label: String get() = ">" }
        data object NotEquals : Keyword() { override val label: String get() = "!=" }
        data object Equals : Keyword() { override val label: String get() = "==" }
        data object And : Keyword() { override val label: String get() = "&&" }
        data object Or : Keyword() { override val label: String get() = "||" }
        data object Not : Keyword() { override val label: String get() = "!" }

        data object Increment : Keyword() { override val label: String get() = "++" }
        data object Decrement : Keyword() { override val label: String get() = "--" }

        data object For : Keyword() { override val label: String get() = "for" }
        data object LBracket : Keyword() { override val label: String get() = "(" }
        data object RBracket : Keyword() { override val label: String get() = ")" }
        data object Assign : Keyword() { override val label: String get() = "=" }
        data object Semicolon : Keyword() { override val label: String get() = ";" }
    }
}
