package news;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class VKNewsManagerTest {
    private VKNewsManager manager;

    @Mock
    private VKNewsClient client;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        manager = new VKNewsManager(client);
        List<Pair<String, Long>> posts = getPosts();

        when(client.countPostsByHashtag(anyString(), anyLong(), anyLong()))
                .thenAnswer(invocation -> {
                    String hashtag = invocation.getArgument(0);
                    Long startTime = invocation.getArgument(1);
                    Long endTime = invocation.getArgument(2);

                    return (int) posts.stream()
                            .filter(i -> (i.getRight() >= startTime && i.getRight() < endTime))
                            .filter(i -> i.getLeft().contains(hashtag))
                            .count();
                });

    }

    @Test
    public void sixHoursTestWithHash() {
        Stream<Integer> res = manager.countPostsByHashtag("#hashtag", 6);
        assertEquals(List.of(0, 2, 0, 0, 1, 1), res.collect(Collectors.toList()));
    }

    @Test
    public void sixHoursTestWithoutHash() {
        Stream<Integer> res = manager.countPostsByHashtag("hashtag", 6);
        assertEquals(List.of(0, 2, 0, 0, 1, 1), res.collect(Collectors.toList()));
    }

    @Test
    public void oneDayTestWithHash() {
        Stream<Integer> res = manager.countPostsByHashtag("#hashtag", 24);
        assertEquals(List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 1, 1),
                res.collect(Collectors.toList()));
    }

    @Test
    public void oneDayTestWithoutHash() {
        Stream<Integer> res = manager.countPostsByHashtag("hashtag", 24);
        assertEquals(List.of(1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 1, 1),
                res.collect(Collectors.toList()));
    }

    private List<Pair<String, Long>> getPosts() {
        Function<Integer, Long> postHoursAgo = x -> LocalDateTime.now().minusHours(x).toEpochSecond(ZoneOffset.UTC);
        return List.of(
                Pair.of("some hashtag some", postHoursAgo.apply(24)),
                Pair.of("hashtag history with #hashtag", postHoursAgo.apply(15)),
                Pair.of("#hashtag", postHoursAgo.apply(7)),
                Pair.of("hashtag not#hashtag", postHoursAgo.apply(7)),
                Pair.of("#HASHTAG something", postHoursAgo.apply(6)),
                Pair.of("some in #some middle of the text", postHoursAgo.apply(5)),
                Pair.of("", postHoursAgo.apply(4)),
                Pair.of("#hash", postHoursAgo.apply(5)),
                Pair.of("#hashtag", postHoursAgo.apply(5)),
                Pair.of("#hashtagg", postHoursAgo.apply(5)),
                Pair.of("Something another", postHoursAgo.apply(3)),
                Pair.of("Yet another #hashtag", postHoursAgo.apply(2)),
                Pair.of("messageWithSpaces #hashtag", postHoursAgo.apply(1)),
                Pair.of("messageWithoutSpaces#hashtag", postHoursAgo.apply(0))
        );
    }
}
