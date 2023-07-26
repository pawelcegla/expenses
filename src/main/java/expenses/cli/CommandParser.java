package expenses.cli;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface CommandParser extends Function<String, Optional<? extends Command>> {}
