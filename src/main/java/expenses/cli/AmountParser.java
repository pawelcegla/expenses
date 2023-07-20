package expenses.cli;

import org.petitparser.context.Context;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import java.math.BigDecimal;

import static org.petitparser.parser.primitive.CharacterParser.digit;
import static org.petitparser.parser.primitive.CharacterParser.of;

public class AmountParser extends Parser {

    private final Parser amountParser;

    public AmountParser() {
        amountParser =
                digit().repeat(1, 10).seq(
                        of('.'),
                        digit().times(2)
                )
                        .flatten()
                        .map((String s) -> new BigDecimal(s));
    }

    @Override
    public Result parseOn(Context context) {
        return amountParser.parseOn(context);
    }

    @Override
    public Parser copy() {
        return new AmountParser();
    }
}
