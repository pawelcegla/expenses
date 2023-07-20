package expenses.cli;

import org.petitparser.context.Context;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static org.petitparser.parser.primitive.CharacterParser.digit;
import static org.petitparser.parser.primitive.CharacterParser.of;

public class DateParser extends Parser {

    private final Parser dateParser;

    public DateParser() {
        dateParser =
                digit().times(4).seq(
                        of('-'),
                        digit().times(2),
                        of('-'),
                        digit().times(2)
                )
                        .flatten()
                        .map(DateParser::tryParseDate);
    }

    private static Optional<LocalDate> tryParseDate(String input) {
        try {
            return Optional.of(LocalDate.parse(input));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    @Override
    public Result parseOn(Context context) {
        return dateParser.parseOn(context);
    }

    @Override
    public Parser copy() {
        return new DateParser();
    }
}
