package expenses.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.petitparser.parser.Parser;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateParserTest {

    private Parser sut;

    @BeforeEach
    public void setUp() {
        sut = new DateParser();
    }

    @Test
    public void shouldParseProperlyFormattedIsoLocalDateSuccessfully() {
        var res = sut.parse("2023-07-20");
        assertTrue(res.isSuccess());
        Optional<LocalDate> val = res.get();
        assertTrue(val.isPresent());
        assertEquals(LocalDate.of(2023, 7, 20), val.get());
    }

    @Test
    public void shouldFailParsingOnLettersInDate() {
        var res = sut.parse("2023-o7-20");
        assertTrue(res.isFailure());
    }

    @Test
    public void shouldFailParsingOnNationalDateFormat() {
        var res = sut.parse("20.07.2023");
        assertTrue(res.isFailure());
    }

    @Test
    public void shouldFailObtainingValueWhenMonthAndDayExceedAllowedValues() {
        var res = sut.parse("2023-45-67");
        assertTrue(res.isSuccess());
        Optional<LocalDate> val = res.get();
        assertTrue(val.isEmpty());
    }
}
