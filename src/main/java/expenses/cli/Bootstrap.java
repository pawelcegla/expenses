package expenses.cli;

import expenses.Storage;
import org.sqlite.JDBC;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class Bootstrap {

    public static final String DATABASE_DEFAULT_URL = "jdbc:sqlite:expenses.db";

    public static void main(String... args) throws SQLException, IOException {
        DriverManager.registerDriver(new JDBC());
        if (System.getProperty("script") != null) {
            switch (System.getProperty("script")) {
                case "e" -> storeExpense(DATABASE_DEFAULT_URL, System.getProperties());
                case "d" -> storeCurrentDate(DATABASE_DEFAULT_URL, System.getProperties());
            }
        } else {
            Repl.create().call();
        }

    }

    @FunctionalInterface
    private interface StorageConsumer { void accept(Storage s) throws SQLException; }

    static void executeInDatabase(String url, StorageConsumer consumer) throws SQLException {
        try (var c = DriverManager.getConnection(url)) {
            var s = new Storage(c);
            consumer.accept(s);
        }
    }

    private static final Pattern hyphenedWords = Pattern.compile("\\p{Lower}+(-\\p{Lower}+)*");

    static String[] validate(String... tags) throws IllegalArgumentException {
        for (var tag : tags) {
            if (!hyphenedWords.matcher(tag).matches()) {
                throw new IllegalArgumentException(String.format("tag: %s", tag));
            }
        }
        return tags;
    }

    static void storeExpense(String url, Properties props) throws SQLException {
        executeInDatabase(url, storage ->
                storage.add(
                        LocalDate.parse(props.getProperty("date"), ISO_LOCAL_DATE),
                        new BigDecimal(props.getProperty("amount")),
                        props.getProperty("description"),
                        validate(props.getProperty("tags").split(" "))));
    }

    static void storeCurrentDate(String url, Properties props) throws SQLException {
        executeInDatabase(url, storage ->
                storage.currentDate(LocalDate.parse(props.getProperty("date"), ISO_LOCAL_DATE)));
    }
}
