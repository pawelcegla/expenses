package expenses.cli;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

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

    @Command(command = "d", group = "Expenses", description = "Store current date")
    String storeCurrentDate(@Option(required = true, description = "date") LocalDate date) {

        var n = jdbc.update(
                """
                        INSERT INTO current_date(rowid, date) VALUES (1, :date)
                        ON CONFLICT (rowid) DO UPDATE SET date = :date;""",
                Map.of("date", date.toString())
        );
        return String.format("storeCurrentDate: n='%d'", n);
    }

    private static String toJsonArray(Set<Tag> tags) {
        return tags.stream()
                .map(t -> "\"" + t.name() + "\"")
                .collect(Collectors.joining(",", "[", "]"));
    }

    @Command(command = "t", group = "Expenses", description = "Display existing tags")
    String displayTags() {
        var tags = jdbc.queryForList(
                "SELECT DISTINCT t.value FROM expenses e, json_each(e.tags) t ORDER BY 1 ASC;",
                Map.of(),
                String.class
        );
        return String.join("\n", tags);
    }

    private record Expense(String rowid, String date, String amount, String description, String tags) {}

    @Command(command = "s", group = "Expenses", description = "Full text search")
    String fullTextSearch(@Option(required = true, description = "query") String query) {
        var h = new LinkedHashMap<String, Object>();
        h.put("rowid", "Id");
        h.put("date", "Date");
        h.put("amount", "Amount");
        h.put("description", "Description");
        h.put("tags", "Tags");
        var results = jdbc.query("""
                SELECT e.rowid, e.date, e.amount, e.description, e.tags FROM expenses e INNER JOIN fts_expenses(:query) f ON e.rowid = f.rowid ORDER BY e.date ASC;
                """, Map.of("query", query), new DataClassRowMapper<>(Expense.class)).stream()
                .map(e -> new Expense(e.rowid, e.date, e.amount, e.description, unjsonify(e.tags)))
                .toList();
        var tm = new BeanListTableModel<>(results, h);
        var tb = new TableBuilder(tm).addHeaderBorder(BorderStyle.fancy_double).addInnerBorder(BorderStyle.fancy_light);
        return tb.build().render(120);
    }

    private String unjsonify(String tags) {
        return stream(tags.substring(1, tags.length() - 1).split(",")).map(t -> t.substring(1, t.length() - 1)).collect(Collectors.joining(", "));
    }
}
