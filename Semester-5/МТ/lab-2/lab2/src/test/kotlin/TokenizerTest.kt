import Token.*
import Token.Keyword.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertTrue

class TokenizerTest {
    private lateinit var producer: StringProducer
    private lateinit var lexer: Flow<Token>

    private fun initializeFromStr(str: String) {
        producer = StringProducer(str.reader().buffered())
        lexer = newLexer(producer)
    }

    private fun consume(): List<Token> = runBlocking {
        val x = lexer.toList()
//        println(x)
        return@runBlocking x
    }
    private fun compareResult(template: List<Token>): Boolean {
        val result = consume()
        for (i in 0..<template.size) {
            if (template[i].label != result[i].label) {
                return false
            }
        }
        return true
    }

    /**
     * Complete supported for statement.
     * Uses all the supported token types
     * Is compared with the template
     */
    @Test
    fun `Successful complete`() {
        val testString = "for(int x=0;x<10;x++)"
        initializeFromStr(testString)
        assertTrue(compareResult(listOf(For, LBracket, Keyword.Int, Word("x"), Assign, Number(0), Semicolon, Word("x"), LessThan, Number(10), Semicolon, Word("x"), Increment, RBracket)))
    }

    /**
     * Same as previous, but with a lot of whitespaces
     * Should produce the same result
     */
    @Test
    fun `Successful complete Whitespace`() {
        val testString = "   for    \n  (   int   x  =  0  ;  x  <  10  ;  x ++  )   "
        initializeFromStr(testString)
        assertTrue(compareResult(listOf(For, LBracket, Keyword.Int, Word("x"), Assign, Number(0), Semicolon, Word("x"), LessThan, Number(10), Semicolon, Word("x"), Increment, RBracket)))
    }

    /**
     * A long logic expression demonstrating all possible operations and priorities
     */
    @Test
    fun `Successful logic`() {
        val testString = "(x<0)&&y>z||z<=1&&z>=2||!z!=(!y&&!(1==0&&3<2))"
        initializeFromStr(testString)
        assertTrue(
            compareResult(
                listOf(
                    LBracket, Word("x"), LessThan, Number(0),
                    RBracket, And, Word("y"), GreaterThan, Word("z"), Or, Word("z"),
                    LessEquals, Number(1), And, Word("z"), GreaterEquals, Number(2),
                    Or, Not, Word("z"), NotEquals, LBracket, Not, Word("y"), And, Not, LBracket,
                    Number(1), Equals, Number(0), And, Number(3), LessThan, Number(2),
                    RBracket, RBracket,
                ),
            ),
        )
    }

    /**
     * Same as above but with whitespaces
     */
    @Test
    fun `Successful logic whitespace`() {
        val testString = " ( x < 0) && y > z || z <= 1 && z >= 2 || ! z != ( ! y && ! ( 1 == 0 && 3 < 2 ) ) "
        initializeFromStr(testString)
        assertTrue(
            compareResult(
                listOf(
                    LBracket, Word("x"), LessThan, Number(0),
                    RBracket, And, Word("y"), GreaterThan, Word("z"), Or, Word("z"),
                    LessEquals, Number(1), And, Word("z"), GreaterEquals, Number(2),
                    Or, Not, Word("z"), NotEquals, LBracket, Not, Word("y"), And, Not, LBracket,
                    Number(1), Equals, Number(0), And, Number(3), LessThan, Number(2),
                    RBracket, RBracket,
                ),
            ),
        )
    }

    /**
     * Unsupported brackets symbols, should fail
     */
    @Test
    fun `Fail syntax`() {
        val testString = "for[int x=0;x<10;x++]"
        initializeFromStr(testString)
        assertFails { consume() }
    }

    /**
     * Unsupported separator symbol, should fail
     */
    @Test
    fun `Fail syntax2`() {
        val testString = "for(int x=0,x<10,x++)"
        initializeFromStr(testString)
        assertFails { consume() }
    }

    /**
     * Unsupported operator instead of increment, should fail
     */
    @Test
    fun `Fail increment`() {
        val testString = "for(int x=0;x<10,x+-)"
        initializeFromStr(testString)
        assertFails { consume() }
    }

    /**
     * Variable names should support alphanumeric format (since second symbol)
     */
    @Test
    fun `Success word`() {
        val testString = "a1b2a3c4a5b6a7"
        initializeFromStr(testString)
        assertTrue(compareResult(listOf(Word(testString))))
    }
}
