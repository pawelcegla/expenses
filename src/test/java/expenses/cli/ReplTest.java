package expenses.cli;

import org.junit.jupiter.api.BeforeEach;
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

    private StringWriter out;

    @BeforeEach
    public void setup() {
        out = new StringWriter();
    }

    @Test
    public void shouldProcessEmptyStream() {
        var sut = Repl.fromStreams(new StringReader(""), out);
        assertDoesNotThrow(sut::call);
        assertEquals("", out.toString());
    }

    @Test
    public void shouldProcessSingleExitCommand() {
        var sut = Repl.fromStreams(new StringReader(":wq"), out);
        assertDoesNotThrow(sut::call);
        assertEquals("", out.toString());
    }

    private Reader testReader(String separator, String... lines) {
        return new StringReader(String.join(separator, lines));
    }

    @ParameterizedTest
    @MethodSource("possibleLineSeparators")
    public void shouldProcessRegularCommandWithExit(String sep) {
        var sut =
                Repl.fromStreams(testReader(sep, "ping", ":wq"), out)
                        .add(s -> new Ok<>(() -> "pong"));
        assertDoesNotThrow(sut::call);
        assertEquals("pong" + platformSeparator, out.toString());
    }

    @ParameterizedTest
    @MethodSource("possibleLineSeparators")
    public void shouldProcessRegularCommands(String sep) {
        var sut =
                Repl.fromStreams(testReader(sep, "ping", "ping"), out)
                        .add(s -> new Ok<>(() -> "pong"));
        assertDoesNotThrow(sut::call);
        assertEquals("pong" + platformSeparator + "pong" + platformSeparator, out.toString());
    }

    @ParameterizedTest
    @MethodSource("possibleLineSeparators")
    public void shouldReportAmbiguousCommandParsers(String sep) {
        var sut =
                Repl.fromStreams(testReader(sep, "ping"), out)
                        .add(s -> new Ok<>(() -> "pong"))
                        .add(s -> new Ok<>(() -> "gong"));
        assertDoesNotThrow(sut::call);
        assertEquals("ambiguous command parser configurations" + platformSeparator, out.toString());
    }

    @ParameterizedTest
    @MethodSource("possibleLineSeparators")
    public void shouldReportUnknownCommands(String sep) {
        var sut =
                Repl.fromStreams(testReader(sep, "zing"), out)
                        .add(s -> "ping".equals(s) ? new Ok<>(() -> "pong") : CommandParsingResult.unknown());
        assertDoesNotThrow(sut::call);
        assertEquals("unknown command" + platformSeparator, out.toString());
    }

    @ParameterizedTest
    @MethodSource("possibleLineSeparators")
    public void shouldReportCommandSyntaxErrors(String sep) {
        var sut =
                Repl.fromStreams(testReader(sep, "ping"), out)
                        .add(s -> "ping".equals(s) ? new SyntaxError<>(() -> "?SYNTAX  ERROR") : CommandParsingResult.unknown());
        assertDoesNotThrow(sut::call);
        assertEquals("?SYNTAX  ERROR" + platformSeparator, out.toString());
    }

    private static Stream<Arguments> possibleLineSeparators() {
        return Stream.of(
                arguments(named("CR", "\r")),
                arguments(named("LF", "\n")),
                arguments(named("CRLF", "\r\n"))
        );
    }
}
