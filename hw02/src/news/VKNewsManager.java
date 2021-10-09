package news;

import config.Config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.stream.Stream;

public class VKNewsManager {
    private final VKNewsClient client;

    public VKNewsManager(VKNewsClient client) {
        this.client = client;
    }

    public Stream<Integer> countPostsByHashtag(String hashtag, Integer hours) {
        final long secondsPerHour = 60 * 60;
        int[] res = new int[hours];
        long time = LocalDateTime.now()
                .minusHours(hours).toEpochSecond(ZoneOffset.UTC);
        for (int i = 0; i < hours; ++i) {
            res[i] = client.countPostsByHashtag(hashtag,
                    time, time + secondsPerHour);
            time += secondsPerHour;
        }
        return Arrays.stream(res).boxed();
    }

    public static void main(String[] args) throws IOException {
        String hashtag = '#' + args[0];
        Integer pastHours = Integer.parseInt(args[1]);
        Config config = new Config();
        VKNewsClient vkNewsClient = new VKNewsClient("api.vk.com",
                null,
                config.getAccessKey(),
                config.getApiVersion());
        new VKNewsManager(vkNewsClient)
                .countPostsByHashtag(hashtag, pastHours)
                .forEach(System.out::println);
    }
}
