package expenses.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class CommandLineInterfaceTest {

    private Properties props;

    @BeforeEach
    void setup() {
        props = new Properties();
    }

    @Test
    void shouldFailOnIncorrectDate() {
        props.setProperty("script", "e");
        props.setProperty("date", "03/24/23");
        assertThrowsExactly(
                DateTimeParseException.class,
                () -> Bootstrap.storeExpense("jdbc:sqlite::memory:", props));
    }

    @Test
    void shouldFailOnIncorrectAmount() {
        props.setProperty("script", "e");
        props.setProperty("date", "2023-03-24");
        props.setProperty("amount", "0xCAFEBABE");
        assertThrowsExactly(
                NumberFormatException.class,
                () -> Bootstrap.storeExpense("jdbc:sqlite::memory:", props));
    }

    @Test
    void shouldFailOnIncorrectTag() {
        props.setProperty("script", "e");
        props.setProperty("date", "2023-03-24");
        props.setProperty("amount", "123.45");
        props.setProperty("description", "food");
        props.setProperty("tags", "Walmart ;-)");
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> Bootstrap.storeExpense("jdbc:sqlite::memory:", props));
    }

    @Test
    void shouldFailOnIncorrectCurrentDate() {
        props.setProperty("script", "d");
        props.setProperty("date", "04/03/2023");
        assertThrowsExactly(
                DateTimeParseException.class,
                () -> Bootstrap.storeCurrentDate("jdbc:sqlite::memory:", props));
    }
}