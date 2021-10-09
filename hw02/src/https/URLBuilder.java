package https;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class URLBuilder {
    private String buildUrl(String host,
                            Integer port,
                            String schema,
                            String path,
                            List<Pair<String, String>> parameters) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(schema);
        builder.setHost(host);
        if (port != null) {
            builder.setPort(port);
        }
        if (path != null) {
            builder.setPath(path);
        }
        if (parameters != null) {
            for (var param : parameters) {
                builder.setParameter(param.getLeft(), param.getRight());
            }
        }

        try {
            return builder.build().toURL().toString();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException("Can not build an URL", e);
        }
    }

    public String buildHttpUrl(String host,
                               Integer port,
                               String path,
                               List<Pair<String, String>> parameters) {
        return buildUrl(host, port, "http", path, parameters);
    }

    public String buildHttpsUrl(String host,
                                Integer port,
                                String path,
                                List<Pair<String, String>> parameters) {
        return buildUrl(host, port, "https", path, parameters);
    }
}
