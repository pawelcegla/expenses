package expenses.cli;

import expenses.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.petitparser.parser.Parser;

import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TagsParserTest {

    private Parser sut;

    @BeforeEach
    public void setUp() {
        this.sut = new TagsParser();
    }

    private Set<Tag> tags(String... names) {
        return stream(names).map(Tag::new).collect(toUnmodifiableSet());
    }

    @Test
    public void shouldParseEmptyTagSet() {
        var res = sut.parse("[]");
        assertTrue(res.isSuccess());
        Set<Tag> val = res.get();
        assertEquals(tags(), val);
    }

    @Test
    public void shouldParseASingleTag() {
        var res = sut.parse("tag-name-a");
        assertTrue(res.isSuccess());
        Set<Tag> val = res.get();
        assertEquals(tags("tag-name-a"), val);
    }

    @Test
    public void shouldParseManyTags() {
        var res = sut.parse("tag-name-a,tag-name-b,tag-name-c");
        assertTrue(res.isSuccess());
        Set<Tag> val = res.get();
        assertEquals(tags("tag-name-a", "tag-name-b", "tag-name-c"), val);
    }
}
