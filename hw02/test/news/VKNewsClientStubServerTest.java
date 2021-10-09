package news;

import com.xebialabs.restito.semantics.Call;
import com.xebialabs.restito.server.StubServer;
import org.apache.commons.lang3.tuple.Pair;
import org.glassfish.grizzly.http.Method;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.*;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VKNewsClientStubServerTest {
    private static final int PORT = 32453;
    private final VKNewsClient client =
            new VKNewsClient("localhost", PORT, "", "");

    private static final long minTime = 10L;
    private static final long maxTime = 100L;
    private static final List<Pair<String, Long>> posts = List.of(
            Pair.of("some hashtag some", 60L),
            Pair.of("hashtag history with #hashtag", 55L),
            Pair.of("#hashtag", 50L),
            Pair.of("hashtag not#hashtag", 45L),
            Pair.of("#HASHTAG something", 45L),
            Pair.of("some in #some middle of the text", 40L),
            Pair.of("", 38L),
            Pair.of("#hash", 30L),
            Pair.of("#hashtag", 30L),
            Pair.of("#hashtagg", 30L),
            Pair.of("Something another", 23L),
            Pair.of("Yet another #hashtag", 17L),
            Pair.of("messageWithSpaces #hashtag", 13L),
            Pair.of("messageWithoutSpaces#hashtag", 11L)
    );

    @Test
    public void getCountOfSimpleHashTagTest() {
        testWithGivenTimesAndHashtag("#hashtag", minTime, maxTime);
    }

    @Test
    public void getNotAllPostsBecauseOfTimePeriodTest() {
        testWithGivenTimesAndHashtag("#hashtag", 17L, 17L);
    }

    @Test
    public void getCountsWithoutHashtagTest() {
        testWithGivenTimesAndHashtag("hashtag", minTime, maxTime);
    }

    @Test
    public void getEmptyHashtagCountsTest() {
        testWithGivenTimesAndHashtag("", minTime, maxTime);
    }

    private static class HasParameterPredicate implements Predicate<Call> {

        private final String parameterName;

        private HasParameterPredicate(String parameterName) {
            this.parameterName = parameterName;
        }

        @Override
        public boolean test(Call call) {
            return call.getParameters().containsKey(parameterName);
        }
    }

    private void testWithGivenTimesAndHashtag(String hashtag, Long startTime, Long endTime) {
        withStubServer(s -> {
            int rightAnswer = (int) posts.stream()
                    .filter(i -> (i.getRight() >= startTime && i.getRight() < endTime))
                    .filter(i -> i.getLeft().contains(hashtag))
                    .count();
            whenHttp(s)
                    .match(method(Method.GET),
                            startsWithUri("/method/newsfeed.search"),
                            parameter("start_time", startTime.toString()),
                            parameter("end_time", endTime.toString()),
                            parameter("q", hashtag),
                            custom(new HasParameterPredicate("access_token")),
                            custom(new HasParameterPredicate("v"))
                    )
                    .then(stringContent(buildCorrectResponseWithCount(rightAnswer)));

            int result = client.countPostsByHashtag(hashtag, startTime, endTime);
            assertEquals(rightAnswer, result);
        });
    }

    private String buildCorrectResponseWithCount(int count) {
        return "{\n" +
                "    \"response\": {\n" +
                "        \"count\": " + count + ",\n" +
                "        \"items\": [],\n" +
                "        \"total_count\": " + count + "\n" +
                "    }\n" +
                "}";
    }

    private void withStubServer(Consumer<StubServer> callback) {
        StubServer stubServer = null;
        try {
            stubServer = new StubServer(VKNewsClientStubServerTest.PORT).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }
}
