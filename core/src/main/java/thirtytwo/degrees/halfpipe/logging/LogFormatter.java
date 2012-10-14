package thirtytwo.degrees.halfpipe.logging;

// see original at dropwizard
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;

import java.util.TimeZone;

public class LogFormatter extends PatternLayout {
    public LogFormatter(LoggerContext context, TimeZone timeZone) {
        super();
        setOutputPatternAsHeader(false);
        getDefaultConverterMap().put("ex", PrefixedThrowableProxyConverter.class.getName());
        setPattern("%-5p [%d{ISO8601," + timeZone.getID() + "}] %c: %m\n%ex");
        setContext(context);
    }

}
