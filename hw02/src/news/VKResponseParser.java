package news;

import com.google.gson.*;
import exception.ParserException;

public class VKResponseParser {
    private static final String COUNT_NAME = "total_count";
    private static final String RESPONSE_NAME = "response";

    public int getCountFromResponse(String response) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject rawResponse = parser.parse(response).getAsJsonObject();
            JsonObject fieldResponse = rawResponse.getAsJsonObject(RESPONSE_NAME);
            return fieldResponse
                    .get(COUNT_NAME)
                    .getAsInt();
        } catch (JsonSyntaxException | NullPointerException e) {
            throw new ParserException("Parsing error happened while trying to get counts of found posts", e);
        }
    }
}
