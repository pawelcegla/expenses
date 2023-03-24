package expenses;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class Cli {

    public static void main(String... args) throws SQLException {
        run("jdbc:sqlite:expenses.db", args);
    }

    static void run(String url, String... args) throws SQLException {
        try (var c = DriverManager.getConnection(url)) {
            var s = new Storage(c);
            s.add(
                    LocalDate.parse(args[0], ISO_LOCAL_DATE),
                    new BigDecimal(args[1]),
                    args[2],
                    Arrays.copyOfRange(args, 3, args.length));
        }
    }
}
