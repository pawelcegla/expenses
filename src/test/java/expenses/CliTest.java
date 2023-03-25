package expenses;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class CliTest {

    @Test
    public void shouldFailOnIncorrectDate() {
        assertThrowsExactly(DateTimeParseException.class, () -> {
           Cli.run("jdbc:sqlite::memory:", "24 marca 2023");
        });
    }

    @Test
    public void shouldFailOnIncorrectAmount() {
        assertThrowsExactly(NumberFormatException.class, () -> {
           Cli.run("jdbc:sqlite::memory:", "2023-03-24", "0xCAFEBABE");
        });
    }

    @Test
    public void shouldFailOnIncorrectTag() {
        assertThrowsExactly(IllegalArgumentException.class, () -> {
           Cli.run("jdbc:sqlite::memory:", "2023-03-24", "123.45", "jedzenie", "Zakupy w Biedronce :-)");
        });
    }
}
