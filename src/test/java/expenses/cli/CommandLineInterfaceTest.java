package expenses.cli;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class CommandLineInterfaceTest {

    @Test
    public void shouldFailOnIncorrectDate() {
        assertThrowsExactly(
                DateTimeParseException.class,
                () -> Bootstrap.run("jdbc:sqlite::memory:", "24 marca 2023"));
    }

    @Test
    public void shouldFailOnIncorrectAmount() {
        assertThrowsExactly(
                NumberFormatException.class,
                () -> Bootstrap.run("jdbc:sqlite::memory:", "2023-03-24", "0xCAFEBABE"));
    }

    @Test
    public void shouldFailOnIncorrectTag() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> Bootstrap.run("jdbc:sqlite::memory:", "2023-03-24", "123.45", "jedzenie", "Zakupy w Biedronce :-)"));
    }
}
