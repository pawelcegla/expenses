package expenses.cli;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WrappedInTryWithResourcesTest {

    private record Mock(Runnable r) implements AutoCloseable {

        @Override
        public void close() {
            r.run();
        }
    }

    @Test
    public void shouldWrapCallableInNestedTryWithResources() throws Exception {
        final List<Integer> out = new ArrayList<>();
        new WrappedInTryWithResources<>(
                List.of(
                    new Mock(() -> out.add(21906)),
                    new Mock(() -> out.add(62061)),
                    new Mock(() -> out.add(14288)),
                    new Mock(() -> out.add(46220)),
                    new Mock(() -> out.add(52411))),
                () -> {
                    out.add(65494);
                    return null;
                }).call();
        assertEquals(List.of(65494, 52411, 46220, 14288, 62061, 21906), out);
    }
}
