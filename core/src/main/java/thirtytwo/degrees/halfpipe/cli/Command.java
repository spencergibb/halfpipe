package thirtytwo.degrees.halfpipe.cli;

import static java.lang.String.format;
import static thirtytwo.degrees.halfpipe.cli.HelpUtils.*;
import org.apache.commons.cli.*;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 9:27 PM
 */
// see original https://github.com/codahale/dropwizard/blob/master/dropwizard-core/src/main/java/com/yammer/dropwizard/cli/Command.java
public abstract class Command {
    protected String name;
    protected String description;

    protected Command(String name, String description) {
        Assert.hasText(name);
        Assert.hasText(description);
        this.name = name;
        this.description = description;
    }

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    protected String getSyntax() {
        return "[options]";
    }

    protected String getUsage(Class<?> klass) {
        return format("%s %s %s", new JarLocation(klass), getName(), getSyntax());
    }


    protected Options getOptions() {
        return new Options();
    }

    protected final Options getOptionsWithHelp() {
        Options options = new Options();
        for (Option option: (Collection<Option>)getOptions().getOptions()) {
            options.addOption(option);
        }
        addHelp(options);
        return options;
    }

    public abstract void run(CommandLine commandLine) throws Exception;
}
