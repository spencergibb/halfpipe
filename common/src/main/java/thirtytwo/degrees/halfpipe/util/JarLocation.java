package thirtytwo.degrees.halfpipe.util;

import java.io.File;
import java.net.URL;

/**
 * A class which encapsulates the location on the local filesystem of the JAR in which the current
 * code is executing.
 */
//see original https://github.com/codahale/dropwizard/blob/master/dropwizard-core/src/main/java/com/yammer/dropwizard/util/JarLocation.java
public class JarLocation {
    private final Class<?> klass;

    public JarLocation(Class<?> klass) {
        this.klass = klass;
    }

    @Override
    public String toString() {
        final URL location = klass.getProtectionDomain().getCodeSource().getLocation();
        try {
            final String jar = new File(location.getFile()).getName();
            if (jar.endsWith(".jar")) {
                return jar;
            }
            return "project.jar";
        } catch (Exception ignored) {
            return "project.jar";
        }
    }
}
