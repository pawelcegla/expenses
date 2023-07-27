package expenses.cli;

import java.util.stream.Stream;

public record Ok<C extends Command>(C command) implements CommandParsingResult<C> {

    @Override
    public Stream<C> stream() {
        return Stream.of(command);
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public boolean isSyntaxError() {
        return false;
    }

    @Override
    public boolean isUnknown() {
        return false;
    }
}
