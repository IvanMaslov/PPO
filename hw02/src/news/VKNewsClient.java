package news;

import https.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class VKNewsClient {
    private final VKResponseParser parser;
    private final String host;
    private final Integer port;
    private final String accessKey;
    private final String apiVersion;
    private final URLReader reader;
    private final URLBuilder builder;

    public VKNewsClient(String host,
                        Integer port,
                        String accessKey,
                        String apiVersion) {
        this.parser = new VKResponseParser();
        this.host = host;
        this.port = port;
        this.accessKey = accessKey;
        this.apiVersion = apiVersion;
        this.reader = new URLReader();
        this.builder = new URLBuilder();
    }

    public int countPostsByHashtag(String hashtag,
                                   Long startTime,
                                   Long endTime) {
        String response = reader.readAsText(makeRequest(hashtag, startTime, endTime));
        return parser.getCountFromResponse(response);
    }

    private String makeRequest(String hashtag,
                               Long startTime,
                               Long endTime) {
        List<Pair<String, String>> parameters = List.of(
                Pair.of("q", hashtag),
                Pair.of("access_token", accessKey),
                Pair.of("v", apiVersion),
                Pair.of("count", "0"),
                Pair.of("start_time", startTime.toString()),
                Pair.of("end_time", endTime.toString())
        );
        return builder.buildHttpUrl(host, port, "/method/newsfeed.search", parameters);
    }
}
