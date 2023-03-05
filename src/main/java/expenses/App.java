package expenses;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws SQLException {

        try (var c = DriverManager.getConnection("jdbc:sqlite::memory:");
             var s = c.createStatement();
             var rs = s.executeQuery("SELECT sqlite_version();")) {

            rs.next();
            System.out.println(rs.getString(1));
        }
    }
}
