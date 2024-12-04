import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.Reader
import kotlin.test.Test
import kotlin.test.assertFails

class ParserTest {
    private lateinit var reader: Reader
    private lateinit var parser: Parser
    private fun initializeFromStr(str: String) {
        reader = str.reader().buffered()
        parser = Parser(reader)
    }

    private fun parse(): Tree {
        return runBlocking { return@runBlocking parser.parse() }
    }

    /**
     * Complete for statement that contains all
     * supported types of tokens
     */
    @Test
    fun `Successful complete`() {
        val testString = "for(int x=0;x<10;x++)"
        initializeFromStr(testString)
        assertDoesNotThrow { parse() }
    }

    /**
     * Complete for statement that contains all
     * supported types of tokens
     */
    @Test
    fun `Successful complete logic`() {
        val testString = "for(int x=0;(x<0)&&y>z||z<=1&&z>=2||!z!=(!y&&!(1==0&&3<2));x++)"
        initializeFromStr(testString)
        assertDoesNotThrow { parse() }
    }

    /**
     * Too much brackets, should fail
     */
    @Test
    fun `Fail logic mismatched too much brackets`() {
        val testString = "for(int x=0;(x<0)&&y>z||z<=1&&z>=2||!z!=(!y&&!(1==0&&3<2)));x++)"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * Not enough brackets, should fail
     */
    @Test
    fun `Fail logic mismatched not enough brackets`() {
        val testString = "for(int x=0;(x<0)&&y>z||z<=1&&z>=2||!z!=(!y&&!(1==0&&3<2);x++)"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * &| instead of &&, should fail
     */
    @Test
    fun `Fail logic operator and`() {
        val testString = "for(int x=0;(x<0)&&y>z||z<=1&|z>=2||!z!=(!y&&!(1==0&&3<2));x++)"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * ||| instead of ||, should fail
     */
    @Test
    fun `Fail logic or`() {
        val testString = "for(int x=0;(x<0)&&y>z|||z<=1&&z>=2||!z!=(!y&&!(1==0&&3<2));x++)"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * Assign instead of equals, should fail
     */
    @Test
    fun `Fail logic equals`() {
        val testString = "for(int x=0;(x<0)&&y>z||z<=1&&z>=2||!z!=(!y&&!(1=0&&3<2));x++)"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * ? instead of !, should fail
     */
    @Test
    fun `Fail logic not`() {
        val testString = "for(int x=0;(x<0)&&y>z||z<=1&&z>=2||!z!=(?y&&!(1==0&&3<2));x++)"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * Unsupported type, should fail
     */
    @Test
    fun `Fail type`() {
        val testString = "for(string x=0;x<10;x++)"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * Missing semicolon, should fail
     */
    @Test
    fun `Fail semicolon`() {
        val testString = "for(int = x=0x<10;x++)"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * Wrong brackets, should fail
     */
    @Test
    fun `Fail brackets`() {
        val testString = "for[int x=0;x<10;x++]"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * Incorrect keyword (for), should fail
     */
    @Test
    fun `Fail for`() {
        val testString = "foz(int x=0;x<10;x++)"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * Assignment instead of logic expression, should fail
     */
    @Test
    fun `Fail compare`() {
        val testString = "for(int x=0;x=10;x++)"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * Extra garbage after successful statement, should fail
     */
    @Test
    fun `Fail eof`() {
        val testString = "for(int x=0;x<10;x++))))"
        initializeFromStr(testString)
        assertFails { parse() }
    }

    /**
     * Empty input, should fail
     */
    @Test
    fun `Fail empty`() {
        val testString = ""
        initializeFromStr(testString)
        assertFails { parse() }
    }
}
