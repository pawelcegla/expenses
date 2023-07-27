package expenses.cli;

import java.util.stream.Stream;

public sealed interface CommandParsingResult<C extends Command> permits Ok, SyntaxError, Unknown {

    @SuppressWarnings("unchecked")
    static <C extends Command> CommandParsingResult<C> unknown() {
        return (CommandParsingResult<C>) Unknown.unknown;
    }

    Stream<C> stream();
}
