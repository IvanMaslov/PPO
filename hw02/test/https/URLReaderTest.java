package https;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import extension.HostReachableBeforeAllCallback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(HostReachableBeforeAllCallback.class)
@HostReachableBeforeAllCallback.HostReachable(URLReaderTest.HOST)
class URLReaderTest {
    public static final String HOST = "api.vk.com";
    private final URLReader reader = new URLReader();
    private final URLBuilder urlBuilder = new URLBuilder();

    @Test
    public void nonBlankReadTest() {
        assertDoesNotThrow(() -> {
            final String url = urlBuilder.buildHttpsUrl(HOST, null, "/method/newsfeed.search",
                    List.of(Pair.of("v", "5.131")));
            String text = reader.readAsText(url);
            assertFalse(text.isBlank());
        });
    }
}
