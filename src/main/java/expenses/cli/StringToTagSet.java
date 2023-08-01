package expenses.cli;

import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class StringToTagSet implements Converter<String, Set<Tag>> {

    private final Pattern tagRegex = Pattern.compile("\\[]|[a-z\\-]+(,[a-z\\-]+)*");

    @Override
    public Set<Tag> convert(@Nonnull String source) {
        var m = tagRegex.matcher(requireNonNull(source));
        if (m.matches()) {
            return "[]".equals(source)
                    ? Set.of()
                    : stream(source.split(",")).map(Tag::new).collect(toUnmodifiableSet());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
