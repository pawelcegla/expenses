package expenses.cli;

import expenses.Storage;
import org.sqlite.JDBC;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class Bootstrap {

    public static void main(String... args) throws SQLException {
        DriverManager.registerDriver(new JDBC());
        run("jdbc:sqlite:expenses.db", args);
    }

    private static final Pattern hyphenedWords = Pattern.compile("\\p{Lower}+(-\\p{Lower}+)*");

    static void run(String url, String... args) throws SQLException {
        try (var c = DriverManager.getConnection(url)) {
            var s = new Storage(c);
            s.add(
                    LocalDate.parse(args[0], ISO_LOCAL_DATE),
                    new BigDecimal(args[1]),
                    args[2],
                    validate(Arrays.copyOfRange(args, 3, args.length)));
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
