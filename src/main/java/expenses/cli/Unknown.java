package expenses.cli;

import java.util.stream.Stream;

public final class Unknown<C extends Command> implements CommandParsingResult<C> {

    final static CommandParsingResult<?> unknown = new Unknown<>();

    private Unknown() {}

    @Override
    public Stream<C> stream() {
        return Stream.empty();
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public boolean isSyntaxError() {
        return false;
    }

    @Override
    public boolean isUnknown() {
        return true;
    }
}
