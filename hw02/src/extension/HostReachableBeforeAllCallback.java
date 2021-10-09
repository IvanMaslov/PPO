package extension;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

public class HostReachableBeforeAllCallback implements BeforeAllCallback {
    private final static int TIMEOUT = 1_000;

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    public @interface HostReachable {
        String value();
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!AnnotationSupport.isAnnotated(context.getRequiredTestClass(), HostReachable.class)) {
            throw new Exception("User @HostReachable annotation");
        }
        String host = context.getRequiredTestClass().getAnnotation(HostReachable.class).value();
        Assumptions.assumeTrue(checkHost(host), "Skipped test because host is unreachable");
    }

    private static boolean checkHost(String host) {
        try {
            Process pingProcess = new ProcessBuilder("ping", "-n", "1", host).start();
            return pingProcess.waitFor(TIMEOUT, TimeUnit.MILLISECONDS)
                    || pingProcess.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
