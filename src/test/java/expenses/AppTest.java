package expenses;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple App.
 */
class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    void shouldAnswerWithTrue() {
        assertDoesNotThrow(() -> {
            try (var c = DriverManager.getConnection("jdbc:sqlite::memory:");
                 var s = c.createStatement()) {
                assertEquals("3.41.0", App.sqliteVersion(s));
            }
        });
    }
}
