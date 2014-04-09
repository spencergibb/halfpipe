package halfpipe.logging;

import ch.qos.logback.classic.Level;
import lombok.Data;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 1:13 PM
 */
@Data
public class LogbackEntry {
    private final String name;
    private final Level level;

    public static LogbackEntry valueOf(String s) {
        String[] entryValues = s.split(":");
        if (entryValues.length != 2) {
            throw new IllegalArgumentException(s +" is an illegal loggers entry.  Must be of form 'log.name:LEVEL");
        }
        String name = entryValues[0];
        Level level = Level.valueOf(entryValues[1]);
        return new LogbackEntry(name, level);
    }
}
