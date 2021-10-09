package news;

import exception.ParserException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VKResponseParserTest {
    private final VKResponseParser parser = new VKResponseParser();

    @Test
    public void parseCorrectResponse() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; ++i) {
                int res = parser.getCountFromResponse(buildCorrectResponseWithCount(i));
                assertEquals(i, res);
            }
        });
    }

    @Test
    public void parseErrorResponse() {
        assertThrows(ParserException.class, () -> parser.getCountFromResponse(buildErrorResponse()));
    }

    @Test
    public void parseResponseWithoutCount() {
        assertThrows(ParserException.class, () -> parser.getCountFromResponse(buildIncorrectResponseWithoutCount()));
    }

    private String buildIncorrectResponseWithoutCount() {
        return "{\n" +
                "    \"response\": {\n" +
                "        \"items\": []\n" +
                "    }\n" +
                "}";
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

    private String buildErrorResponse() {
        return "{\n" +
                "    \"error\": {\n" +
                "        \"error_code\": 100,\n" +
                "        \"error_msg\": \"One of the parameters specified was missing or invalid: start_time should be greater or equal to 0\"\n" +
                "    }\n" +
                "}";
    }
}
