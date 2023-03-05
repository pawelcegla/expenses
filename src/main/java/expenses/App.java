package expenses;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws SQLException {
        try (var c = DriverManager.getConnection("jdbc:sqlite::memory:");
             var s = c.createStatement()) {
            System.out.println(sqliteVersion(s));
        }
    }

    static String sqliteVersion(Statement s) throws SQLException {
        try (var rs = s.executeQuery("SELECT sqlite_version();")) {
            rs.next();
            return rs.getString(1);
        }
    }
}
