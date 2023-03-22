package expenses;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

/**
 * Hello world!
 */
public class Cli {

    public static void main(String[] args) throws SQLException {
        execute("jdbc:sqlite:expenses.db", "INSERT INTO expenses VALUES (?, ?, ?, ?);", s -> {
            LocalDate.parse(args[0], ISO_LOCAL_DATE);
            s.setString(1, args[0]);
            s.setBigDecimal(2, new BigDecimal(args[1]));
            s.setString(3, args[2]);
            s.setString(4, stream(args).skip(3).map(t -> '"' + t + '"').collect(joining(",", "[", "]")));
        });
    }

    static void execute(String url, String sql, SetParameters set) throws SQLException {
        try (var c = DriverManager.getConnection(url); var s = c.prepareStatement(sql)) {
            set.apply(s);
            s.execute();
        }
    }
}
