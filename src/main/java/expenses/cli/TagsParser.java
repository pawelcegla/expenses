package expenses.cli;

import expenses.Tag;
import org.petitparser.context.Context;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.StringParser;

import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.petitparser.parser.primitive.CharacterParser.letter;
import static org.petitparser.parser.primitive.CharacterParser.of;

public class TagsParser extends Parser {

    private final Parser tagsParser;

    public TagsParser() {
        var noTags = StringParser.of("[]");
        var singleTagName =
                letter().plus().seq(
                        of('-').seq(letter().plus()).star()
                )
                        .flatten();
        var tagNamesList =
                singleTagName.seq(
                        of(',').seq(singleTagName).star()
                ).flatten();
        tagsParser = noTags.or(tagNamesList).map(TagsParser::tryParseTags);
    }

    private static Set<Tag> tryParseTags(String input) {
        return "[]".equalsIgnoreCase(input) ? Set.of() : stream(input.split(",")).map(Tag::new).collect(toUnmodifiableSet());
    }

    @Override
    public Result parseOn(Context context) {
        return tagsParser.parseOn(context);
    }

    @Override
    public Parser copy() {
        return new TagsParser();
    }
}
