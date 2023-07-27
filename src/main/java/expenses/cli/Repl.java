package expenses.cli;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Repl implements Callable<Void> {

    private final BufferedReader in;
    private final PrintWriter out;
    private final boolean consolePresent;

    private final List<CommandParser> commandParsers;

    private Repl(BufferedReader in, PrintWriter out, boolean consolePresent) {
        this.in = in;
        this.out = out;
        this.consolePresent = consolePresent;
        this.commandParsers = new ArrayList<>();
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

    public Repl add(CommandParser commandParser) {
        this.commandParsers.add(commandParser);
        return this;
    }

    @Override
    public Void call() throws IOException {
        try (in; out) {
            printPromptForConsole();
            out.flush();
            final String[] s = new String[1];
            s[0] = in.readLine();
            while (s[0] != null && !":wq".equalsIgnoreCase(s[0].trim())) {
                var commands =
                        commandParsers.stream()
                                .map(p -> p.apply(s[0]))
                                .flatMap(CommandParsingResult::stream)
                                .toList();
                if (commands.isEmpty()) {
                    out.println("unknown command");
                } else if (commands.size() > 1) {
                    out.println("ambiguous command parser configurations");
                } else {
                    out.println(commands.get(0).call());
                }
                printPromptForConsole();
                out.flush();
                s[0] = in.readLine();
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
