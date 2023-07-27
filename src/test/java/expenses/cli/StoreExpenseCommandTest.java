package expenses.cli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoreExpenseCommandTest {

    private void assertUnknown(CommandParsingResult<? extends Command> r) {
        assertFalse(r.isOk());
        assertFalse(r.isSyntaxError());
        assertTrue(r.isUnknown());
        assertInstanceOf(Unknown.class, r);
    }

    private void assertSyntaxError(CommandParsingResult<? extends Command> r) {
        assertFalse(r.isOk());
        assertTrue(r.isSyntaxError());
        assertFalse(r.isUnknown());
        assertInstanceOf(SyntaxError.class, r);
    }

    private void assertOk(CommandParsingResult<? extends Command> r) {
        assertTrue(r.isOk());
        assertFalse(r.isSyntaxError());
        assertFalse(r.isUnknown());
        assertInstanceOf(Ok.class, r);
    }

    @Test
    public void shouldReturnUnknownWhenParsingDifferentCommand() {
        var res = StoreExpense.tryParse("q w e r t y");
        assertUnknown(res);
    }

    @Test
    public void shouldReturnOkWhenParsingProperCommand() {
        var res = StoreExpense.tryParse("e 2023-07-23 123.45 tag,another-tag some description");
        assertOk(res);
    }

    @Test
    public void shouldFailParsingOnLettersInDate() {
        var res = StoreExpense.tryParse("e 2023-O7-23 123.45 tag,another-tag some description");
        assertSyntaxError(res);
    }

    @Test
    public void shouldFailParsingOnNationalDateFormat() {
        var res = StoreExpense.tryParse("e 23.07.2023 123.45 tag,another-tag some description");
        assertSyntaxError(res);
    }

    @Test
    public void shouldFailObtainingValueWhenMonthAndDayExceedAllowedValues() {
        var res = StoreExpense.tryParse("e 2023-45-67 123.45 tag,another-tag some description");
        assertSyntaxError(res);
    }

    @Test
    public void shouldFailParsingAmountWithoutFractionalDigits() {
        var res = StoreExpense.tryParse("e 2023-07-23 123. tag,another-tag some description");
        assertSyntaxError(res);
    }

    @Test
    public void shouldFailParsingAmountWithoutFractionalPart() {
        var res = StoreExpense.tryParse("e 2023-07-23 123 tag,another-tag some description");
        assertSyntaxError(res);
    }

    @Test
    public void shouldFailParsingAmountWithLetters() {
        var res = StoreExpense.tryParse("e 2023-07-23 I23 tag,another-tag some description");
        assertSyntaxError(res);
    }
}
