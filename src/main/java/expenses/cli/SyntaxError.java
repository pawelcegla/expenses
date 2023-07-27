package expenses.cli;

import java.util.stream.Stream;

public record SyntaxError<C extends Command>(C command) implements CommandParsingResult<C> {

    @Override
    public Stream<C> stream() {
        return Stream.of(command);
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public boolean isSyntaxError() {
        return true;
    }

    @Override
    public boolean isUnknown() {
        return false;
    }
}
