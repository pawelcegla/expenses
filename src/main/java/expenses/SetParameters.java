package expenses;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SetParameters {

    void apply(PreparedStatement s) throws SQLException;
}
