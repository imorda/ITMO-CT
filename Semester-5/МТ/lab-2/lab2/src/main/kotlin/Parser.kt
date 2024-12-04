import kotlinx.coroutines.flow.firstOrNull
import java.io.Reader
import java.text.ParseException

class Tree(val node: String, vararg val children: Tree)

class Parser(reader: Reader) {
    private val producer = StringProducer(reader)
    private val lexer = newLexer(producer)
    private var curToken: Token? = null

    companion object {
        private fun typeChecker(a: Token?, b: Token?): Boolean =
            (a == null && b == null) || (a != null && b != null && (a::class == b::class))
    }

    private suspend fun takeToken(): Token? {
        if (curToken == null) {
            curToken = lexer.firstOrNull()
        }
        return curToken
    }
    private fun success(node: String, vararg children: Tree): Tree {
        val result = Tree(node, *children)
        var lastChild: Tree = result
        while (lastChild.children.isNotEmpty()) {
            lastChild = lastChild.children.last()
        }
        if (lastChild.node != "<nothing>") {
            curToken = null
        }
        return result
    }

    @Throws(ParseException::class)
    private fun assertToken(
        token: Token?,
        template: List<Token?>,
        predicate: (Token?, Token?) -> Boolean = Companion::typeChecker,
    ): String {
        if (template.all { !predicate(token, it) }) {
            throw fail(template)
        }
        return token?.label ?: "<nothing>"
    }

    @Throws(ParseException::class)
    private fun fail(expected: List<Token?>) =
        ParseException("Unexpected ${curToken?.label}, ${expected.joinToString(separator = "/") { it?.label ?: "<nothing>" }} expected at position ", producer.pos())

    @Throws(ParseException::class)
    private fun assertToken(
        token: Token?,
        template: Token?,
        predicate: (Token?, Token?) -> Boolean = Companion::typeChecker,
    ): String {
        return assertToken(token, listOf(template), predicate)
    }

    @Throws(ParseException::class)
    suspend fun Start(): Tree =
        success(
            "Start",
            For(),
            LBracket(),
            Type(),
            Word(),
            Assign(),
            Num(),
            Semicolon(),
            CompA(),
            Semicolon(),
            Word(),
            Inc(),
            RBracket(),
            success(assertToken(takeToken(), null)),
        )

    @Throws(ParseException::class)
    suspend fun Type(): Tree =
        success(
            "Type",
            success(
                assertToken(
                    takeToken(),
                    listOf(Token.Keyword.Int, Token.Keyword.Char, Token.Keyword.Long),
                ),
            ),
        )

    @Throws(ParseException::class)
    suspend fun CompA(): Tree = success(
        "CompA",
        CompB(),
        CompA1(),
    )

    @Throws(ParseException::class)
    suspend fun CompA1(): Tree {
        val first = takeToken()

        return when (first) {
            Token.Keyword.Or -> success(
                "CompA'",
                success(first.label),
                CompB(),
                CompA1(),
            )
            else -> success("CompA'", success("<nothing>"))
        }
    }

    @Throws(ParseException::class)
    suspend fun CompB(): Tree = success(
        "CompB",
        CompC(),
        CompB1(),
    )

    @Throws(ParseException::class)
    suspend fun CompB1(): Tree {
        val first = takeToken()

        return when (first) {
            Token.Keyword.And -> success(
                "CompB'",
                success(first.label),
                CompC(),
                CompB1(),
            )
            else -> success("CompB'", success("<nothing>"))
        }
    }

    @Throws(ParseException::class)
    suspend fun CompC(): Tree = success(
        "CompC",
        CompD(),
        CompC1(),
    )

    @Throws(ParseException::class)
    suspend fun CompC1(): Tree {
        val first = takeToken()

        return when (first) {
            Token.Keyword.Equals -> success(
                "CompC'",
                success(first.label),
                CompD(),
                CompC1(),
            )
            Token.Keyword.NotEquals -> success(
                "CompC'",
                success(first.label),
                CompD(),
                CompC1(),
            )
            else -> success("CompC'", success("<nothing>"))
        }
    }

    @Throws(ParseException::class)
    suspend fun CompD(): Tree = success(
        "CompD",
        CompE(),
        CompD1(),
    )

    @Throws(ParseException::class)
    suspend fun CompD1(): Tree {
        val first = takeToken()

        return when (first) {
            Token.Keyword.LessEquals -> success(
                "CompD'",
                success(first.label),
                CompE(),
                CompD1(),
            )
            Token.Keyword.LessThan -> success(
                "CompD'",
                success(first.label),
                CompE(),
                CompD1(),
            )
            Token.Keyword.GreaterEquals -> success(
                "CompD'",
                success(first.label),
                CompE(),
                CompD1(),
            )
            Token.Keyword.GreaterThan -> success(
                "CompD'",
                success(first.label),
                CompE(),
                CompD1(),
            )
            else -> success("CompD'", success("<nothing>"))
        }
    }

    @Throws(ParseException::class)
    suspend fun CompE(): Tree {
        val first = takeToken()

        return when (first) {
            Token.Keyword.Not -> success(
                "CompE",
                success(first.label),
                CompE(),
            )
            else -> CompT()
        }
    }

    @Throws(ParseException::class)
    suspend fun CompT(): Tree {
        val first = takeToken()

        return when (first) {
            Token.Keyword.LBracket -> success(
                "CompT",
                success(first.label),
                CompA(),
                RBracket(),
            )
            is Token.Number -> success(first.label)
            is Token.Word -> success(first.label)
            else -> throw fail(listOf(Token.Keyword.LBracket, Token.Word("number"), Token.Word("word")))
        }
    }

    @Throws(ParseException::class)
    suspend fun Inc(): Tree =
        success(
            "Inc",
            success(
                assertToken(
                    takeToken(),
                    listOf(
                        Token.Keyword.Increment,
                        Token.Keyword.Decrement,
                    ),
                ),
            ),
        )

    @Throws(ParseException::class)
    suspend fun For(): Tree =
        success(
            "For",
            success(
                assertToken(
                    takeToken(),
                    Token.Keyword.For,
                ),
            ),
        )

    @Throws(ParseException::class)
    suspend fun LBracket(): Tree =
        success(
            "LBracket",
            success(
                assertToken(
                    takeToken(),
                    Token.Keyword.LBracket,
                ),
            ),
        )

    @Throws(ParseException::class)
    suspend fun RBracket(): Tree =
        success(
            "RBracket",
            success(
                assertToken(
                    takeToken(),
                    Token.Keyword.RBracket,
                ),
            ),
        )

    @Throws(ParseException::class)
    suspend fun Assign(): Tree =
        success(
            "Assign",
            success(
                assertToken(
                    takeToken(),
                    Token.Keyword.Assign,
                ),
            ),
        )

    @Throws(ParseException::class)
    suspend fun Semicolon(): Tree =
        success(
            "Semicolon",
            success(
                assertToken(
                    takeToken(),
                    Token.Keyword.Semicolon,
                ),
            ),
        )

    @Throws(ParseException::class)
    suspend fun Word(): Tree =
        success(
            "Word",
            success(
                assertToken(
                    takeToken(),
                    Token.Word("word"),
                ),
            ),
        )

    @Throws(ParseException::class)
    suspend fun Num(): Tree =
        success(
            "Num",
            success(
                assertToken(
                    takeToken(),
                    Token.Number(-1),
                ),
            ),
        )

    suspend fun parse(): Tree {
        return Start()
    }
}
