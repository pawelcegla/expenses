package expenses;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class Storage {

    private final Connection c;

    public Storage(Connection c) {
        this.c = c;
    }

    public void add(LocalDate date, BigDecimal amount, String description, String... tags) throws SQLException {
        try (var s = c.prepareStatement("INSERT INTO expenses VALUES (?, ?, ?, ?);")) {
            s.setString(1, ISO_LOCAL_DATE.format(date));
            s.setBigDecimal(2, amount);
            s.setString(3, description);
            s.setString(4, stream(tags).map(t -> '"' + t + '"').collect(joining(",", "[", "]")));
            s.executeUpdate();
        }
    }

    public void currentDate(LocalDate date) throws SQLException {
        try (var s = c.prepareStatement("INSERT INTO current_date (rowid, date) VALUES (1, ?) ON CONFLICT(rowid) DO UPDATE SET date=excluded.date;")) {
            s.setString(1, ISO_LOCAL_DATE.format(date));
            s.executeUpdate();
        }
    }
}
