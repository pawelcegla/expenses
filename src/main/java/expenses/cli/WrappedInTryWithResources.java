package expenses.cli;

import java.util.Iterator;
import java.util.concurrent.Callable;

public class WrappedInTryWithResources<R extends AutoCloseable> implements Callable<Void> {

    private final Iterable<R> resources;
    private final Callable<Void> action;

    WrappedInTryWithResources(Iterable<R> resources, Callable<Void> action) {
        this.resources = resources;
        this.action = action;
    }

    @Override
    public Void call() throws Exception {
        process(resources.iterator());
        return null;
    }

    private void process(Iterator<R> rs) throws Exception {
        if (rs.hasNext()) {
            try (var ignored = rs.next()) {
                process(rs);
            }
        } else {
            action.call();
        }
    }
}
