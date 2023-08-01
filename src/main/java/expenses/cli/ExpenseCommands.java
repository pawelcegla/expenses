package expenses.cli;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Command
public class ExpenseCommands {

    private final NamedParameterJdbcTemplate jdbc;

    ExpenseCommands(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Command(command = "e", group = "Expenses", description = "Store expense")
    String storeExpense(
            @Option(required = true, description = "date") LocalDate date,
            @Option(required = true, description = "amount") BigDecimal amount,
            @Option(required = true, description = "tags") Set<Tag> tags,
            @Option(required = true, description = "description") String description) {

        var n = jdbc.update(
                """
                        INSERT INTO expenses (date, amount, tags, description)
                        VALUES (:date, :amount, :tags, :description);""",
                Map.of(
                        "date", date.toString(),
                        "amount", amount,
                        "tags", toJsonArray(tags),
                        "description", description
                ));
        var rowid = jdbc.queryForObject("SELECT last_insert_rowid();", Map.of(), Integer.class);
        return String.format("storeExpense: n='%d', rowid='%d'", n, rowid);
    }

    private static String toJsonArray(Set<Tag> tags) {
        return tags.stream()
                .map(t -> "\"" + t.name() + "\"")
                .collect(Collectors.joining(",", "[", "]"));
    }
}
