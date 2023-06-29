package expenses.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ReplTest {

    private final static String platformSeparator = System.getProperty("line.separator");

    @Test
    public void shouldProcessEmptyStream() {
        var out = new StringWriter();
        assertDoesNotThrow(() -> Repl.fromStreams(new StringReader(""), out).call());
        assertEquals("", out.toString());
    }

    @Test
    public void shouldProcessSingleExitCommand() {
        var out = new StringWriter();
        assertDoesNotThrow(() -> Repl.fromStreams(new StringReader(":wq"), out).call());
        assertEquals("", out.toString());
    }

    private Reader testReader(String separator, String... lines) {
        return new StringReader(String.join(separator, lines));
    }

    @ParameterizedTest
    @MethodSource("possibleLineSeparators")
    public void shouldProcessRegularCommandWithExit(String sep) {
        var out = new StringWriter();
        assertDoesNotThrow(() -> Repl.fromStreams(testReader(sep, "a", ":wq"), out).call());
        assertEquals("eval 'a'" + platformSeparator, out.toString());
    }

    @ParameterizedTest
    @MethodSource("possibleLineSeparators")
    public void shouldProcessRegularCommands(String sep) {
        var out = new StringWriter();
        assertDoesNotThrow(() -> Repl.fromStreams(testReader(sep, "foo", "bar"), out).call());
        assertEquals("eval 'foo'" + platformSeparator + "eval 'bar'" + platformSeparator, out.toString());
    }

    private static Stream<Arguments> possibleLineSeparators() {
        return Stream.of(
                arguments(named("CR", "\r")),
                arguments(named("LF", "\n")),
                arguments(named("CRLF", "\r\n"))
        );
    }
}
