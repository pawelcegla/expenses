package expenses.cli;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface Command extends Callable<String> {

    @Override
    String call();
}
