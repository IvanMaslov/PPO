package news;

import config.Config;
import extension.HostReachableBeforeAllCallback;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(HostReachableBeforeAllCallback.class)
@HostReachableBeforeAllCallback.HostReachable(VKNewsClientIntegrationTest.HOST)
class VKNewsClientIntegrationTest {
    public static final String HOST = "api.vk.com";
    private static Config config;

    @BeforeAll
    public static void setupConfiguration() throws IOException {
        config = new Config();
    }

    @Test
    public void getCountsTest() {
        assertDoesNotThrow(() -> {
            VKNewsClient client = new VKNewsClient(HOST,
                    null,
                    config.getAccessKey(),
                    config.getApiVersion());
            Long startTime = LocalDateTime.now().minusHours(24).toEpochSecond(ZoneOffset.UTC);
            Long endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            int foundPosts = client.countPostsByHashtag("#спб", startTime, endTime);
            assertTrue(foundPosts >= 0);
        });
    }
}
