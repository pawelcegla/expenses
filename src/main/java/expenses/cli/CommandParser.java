package expenses.cli;

import java.util.function.Function;

@FunctionalInterface
public interface CommandParser extends Function<String, CommandParsingResult<? extends Command>> {}
