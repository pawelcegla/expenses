package expenses.cli;

import expenses.Storage;
import org.sqlite.JDBC;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class Bootstrap {

    public static void main(String... args) throws SQLException {
        DriverManager.registerDriver(new JDBC());
        run("jdbc:sqlite:expenses.db", System.getProperties());
    }

    private static final Pattern hyphenedWords = Pattern.compile("\\p{Lower}+(-\\p{Lower}+)*");

    static void run(String url, Properties props) throws SQLException {
        try (var c = DriverManager.getConnection(url)) {
            var s = new Storage(c);
            s.add(
                    LocalDate.parse(props.getProperty("date"), ISO_LOCAL_DATE),
                    new BigDecimal(props.getProperty("amount")),
                    props.getProperty("description"),
                    validate(props.getProperty("tags").split(" ")));
        }
    }

    static String[] validate(String... tags) throws IllegalArgumentException {
        for (var tag : tags) {
            if (!hyphenedWords.matcher(tag).matches()) {
                throw new IllegalArgumentException(String.format("tag: %s", tag));
            }
        }
        return tags;
    }
}
