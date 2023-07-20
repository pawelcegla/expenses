package expenses.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.petitparser.parser.Parser;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AmountParserTest {

    private Parser sut;

    @BeforeEach
    public void setUp() {
        sut = new AmountParser();
    }

    @Test
    public void shouldParseProperlyFormattedAmountSuccessfully() {
        var res = sut.parse("123.45");
        assertTrue(res.isSuccess());
        BigDecimal val = res.get();
        assertEquals(new BigDecimal("123.45"), val);
    }

    @Test
    public void shouldFailParsingAmountWithoutFractionalDigits() {
        var res = sut.parse("123.");
        assertTrue(res.isFailure());
    }

    @Test
    public void shouldFailParsingAmountWithoutFractionalPart() {
        var res = sut.parse("123");
        assertTrue(res.isFailure());
    }

    @Test
    public void shouldFailParsingAmountWithLetters() {
        var res = sut.parse("I23");
        assertTrue(res.isFailure());
    }
}
