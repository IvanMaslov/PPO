package https;

import java.io.*;
import java.net.URL;

public class URLReader {
    public String readAsText(String sourceUrl) {
        try {
            URL url = new URL(sourceUrl);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
                StringBuilder buffer = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    buffer.append(inputLine);
                    buffer.append("\n");
                }
                return buffer.toString();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
