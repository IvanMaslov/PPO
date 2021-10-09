package https;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class URLBuilderTest {
    private final URLBuilder builder = new URLBuilder();
    private static final String HOST = "localhost";
    private static final String PATH = "/pth";
    private static final String SCHEMA = "https";
    private static final Integer PORT = 40001;
    private static final List<Pair<String, String>> PARAMETERS = List.of(
            Pair.of("p1", "r1"),
            Pair.of("p2", "r2")
    );
    private static final String PARAMETERS_QUERY = convertParameters(PARAMETERS);

    private static String convertParameters(List<Pair<String, String>> parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Pair<String, String> param : parameters) {
            stringBuilder.append(param.getLeft())
                    .append('=')
                    .append(param.getRight())
                    .append('&');
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("&"));
        return stringBuilder.toString();
    }

    @Test
    public void urlAllTest() {
        final String url = builder.buildHttpsUrl(HOST, PORT, PATH, PARAMETERS);
        final String correctUrl = SCHEMA + "://" + HOST + ':' + PORT + PATH + '?' + PARAMETERS_QUERY;
        assertEquals(correctUrl, url);
    }

    @Test
    public void urlWithoutHostTest() {
        final String url = builder.buildHttpsUrl(null, PORT, PATH, PARAMETERS);
        final String correctUrl = SCHEMA + ":" + PATH + '?' + PARAMETERS_QUERY;
        assertEquals(correctUrl, url);
    }

    @Test
    public void urlWithoutPortTest() {
        final String url = builder.buildHttpsUrl(HOST, null, PATH, PARAMETERS);
        final String correctUrl = SCHEMA + "://" + HOST + PATH + '?' + PARAMETERS_QUERY;
        assertEquals(correctUrl, url);
    }

    @Test
    public void urlWithoutPathTest() {
        final String url = builder.buildHttpsUrl(HOST, PORT, null, PARAMETERS);
        final String correctUrl = SCHEMA + "://" + HOST + ':' + PORT + '?' + PARAMETERS_QUERY;
        assertEquals(correctUrl, url);
    }

    @Test
    public void urlWithoutParametersTest() {
        final String url = builder.buildHttpsUrl(HOST, PORT, PATH, null);
        final String correctUrl = SCHEMA + "://" + HOST + ':' + PORT + PATH;
        assertEquals(correctUrl, url);
    }

    @Test
    public void urlNullTest() {
        assertThrows(RuntimeException.class, () -> builder.buildHttpsUrl(null, null, null, null));
    }
}
