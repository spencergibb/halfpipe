package halfpipe.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A logger class which provides easy access to slf4j's Logger
 *
 * <code>
 *
 * // Explicit class specification
 * private static final Logger LOG = Log.forClass(Thingy.class);
 *
 * // Or, automatic class specification
 * private static final Logger LOG = Log.forThisClass();
 * ...
 *
 * LOG.debug("Simple usage: {} / {}", a, b);
 * LOG.debug("No need for manual arrays: {} - {} - {} - {}", a, b, c, d);
 * LOG.warn(exception, "Exceptions go first but don't prevent message formatting: {}", otherStuff);
 * </code>
 *
 * see original at dropwizard
 */
@SuppressWarnings("UnusedDeclaration")
public class Log {

	/**
	 * Returns a {@link Log} instance for the current class.
	 * The current class is determined in the static context by inspecting the stack.
	 * Further details about this approach may be found
	 * <a href="http://www.javaspecialists.eu/archive/Issue137.html">here</a>.
	 *
	 * @return a {@link Log} instance with the current class name
	 */
	public static Logger forThisClass() {
		Throwable t = new Throwable();
		StackTraceElement directCaller = t.getStackTrace()[1];
		return named(directCaller.getClassName());
	}

	/**
     * Returns a {@link Log} instance for the given class.
     *
     * @param klass    a given class
     * @return a {@link Log} instance with {@code klass}'s name
     */
    public static Logger forClass(Class<?> klass) {
        // a thread-safe SLF4J initialization routine is apparently hard, so I get to do dumb
        // shit like this
        while (true) {
            final Logger logger = LoggerFactory.getLogger(klass);
            if (logger instanceof ch.qos.logback.classic.Logger) {
                return logger;
            }
        }
    }

    /**
     * Returns a {@link Log} instance with the given name.
     *
     * @param name    a given name
     * @return a {@link Log} instance with the given name
     */
    public static Logger named(String name) {
        // a thread-safe SLF4J initialization routine is apparently hard, so I get to do dumb
        // shit like this
        while (true) {
            final Logger logger = LoggerFactory.getLogger(name);
            if (logger instanceof ch.qos.logback.classic.Logger) {
                return logger;
            }
        }
    }
    
}
