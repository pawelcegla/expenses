package expenses.cli;

import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static java.util.Objects.requireNonNull;

public class StringToLocalDate implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(@Nonnull String source) {
        try {
            return LocalDate.parse(requireNonNull(source));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
