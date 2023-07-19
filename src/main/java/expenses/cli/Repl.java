package expenses.cli;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.Callable;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Repl implements Callable<Void> {

    private final BufferedReader in;
    private final PrintWriter out;
    private final boolean consolePresent;

    private Repl(BufferedReader in, PrintWriter out, boolean consolePresent) {
        this.in = in;
        this.out = out;
        this.consolePresent = consolePresent;
    }

    static Repl fromStreams(Reader in, Writer out) {
        return new Repl(
                in instanceof BufferedReader ? ((BufferedReader) in) : new BufferedReader(in),
                out instanceof PrintWriter ? ((PrintWriter) out) : new PrintWriter(out, false),
                false
        );
    }

    static Repl fromConsole(Console con) {
        return new Repl(new BufferedReader(con.reader()), con.writer(), true);
    }

    static Repl fromStdin() {
        return fromStreams(new InputStreamReader(System.in, UTF_8), new OutputStreamWriter(System.out, UTF_8));
    }

    public static Repl create() {
        return System.console() == null ? fromStdin() : fromConsole(System.console());
    }

    @Override
    public Void call() throws IOException {
        try (in; out) {
            printPromptForConsole();
            out.flush();
            var s = in.readLine();
            while (s != null && !":wq".equalsIgnoreCase(s.trim())) {
                out.printf("eval '%s'%n", s);
                printPromptForConsole();
                out.flush();
                s = in.readLine();
            }
        }
        return null;
    }

    private void printPromptForConsole() {
        if (consolePresent) {
            out.print("E:\\>");
        }
    }
}
