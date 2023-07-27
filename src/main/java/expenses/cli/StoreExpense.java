package expenses.cli;

import expenses.Tag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.regex.Pattern;

import static expenses.cli.CommandParsingResult.unknown;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class StoreExpense implements Command {

    private static final Pattern regex = Pattern.compile("e\\h+(?<date>\\d{4}-\\d{2}-\\d{2})\\h+(?<amount>\\d{1,10}\\.\\d{2})\\h+(?<tags>(\\[]|[a-z\\-]+(,[a-z\\-]+)*))\\h+(?<description>\\w+(\\h+\\w+)*)");

    private final LocalDate date;
    private final BigDecimal amount;
    private final Set<Tag> tags;
    private final String description;
    private StoreExpense(LocalDate date, BigDecimal amount, Set<Tag> tags, String description) {
        this.date = date;
        this.amount = amount;
        this.tags = tags;
        this.description = description;
    }

    public static CommandParsingResult<? extends Command> tryParse(String input) {
        if (input.startsWith("e")) {
            var m = regex.matcher(input);
            if (m.matches()) {
                try {
                    return new Ok<>(
                            new StoreExpense(
                                    LocalDate.parse(m.group("date")),
                                    new BigDecimal(m.group("amount")),
                                    "[]".equals(m.group("tags"))
                                            ? Set.of()
                                            : stream(m.group("tags").split(",")).map(Tag::new).collect(toUnmodifiableSet()),
                                    m.group("description")));
                } catch (DateTimeParseException | NumberFormatException e) {
                    return new SyntaxError<>(() -> "?SYNTAX  ERROR");
                }
            } else {
                return new SyntaxError<>(() -> "?SYNTAX  ERROR");
            }
        } else {
            return unknown();
        }
    }

    @Override
    public String call() {
        return String.format("%s %s %s %s %s", getClass().getSimpleName(), date, amount, tags, description);
    }
}
