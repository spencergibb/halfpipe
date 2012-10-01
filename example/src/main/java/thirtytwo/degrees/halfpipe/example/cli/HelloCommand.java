package thirtytwo.degrees.halfpipe.example.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.*;
import org.springframework.stereotype.Component;
import thirtytwo.degrees.halfpipe.cli.Command;
import thirtytwo.degrees.halfpipe.example.resources.Sayer;

import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 10:07 PM
 */
@Component
public class HelloCommand extends Command implements CommandMarker {

    @Inject
    Sayer sayer;

    protected HelloCommand() {
        super("hello", "Say hello to the command line");
    }

    @CliAvailabilityIndicator({"hello"})
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = "hello", help = "Say hello to the shell")
    public String hello(
            @CliOption(key = "message", mandatory = true, help = "The hello world message")
            String message,

            @CliOption(key = "more", mandatory = false,
                    help = "Have some more to say while saying ehllo",
                    specifiedDefaultValue="")
            String more)
    {
        StringBuilder hello = new StringBuilder(sayer.hello());
        hello.append(" with this message, "+message);
        if (!StringUtils.isBlank(more)) {
            hello.append(" "+more);
        }
        return hello.toString();
    }

    public Options getOptions() {
        return new Options().addOption("m", "more", true, "add more to hello");
    }

    public void run(CommandLine commandLine) {
        StringBuilder hello = new StringBuilder(sayer.hello());
        hello.append(" CLI");

        if (commandLine.hasOption("more")) {
            hello.append(" ");
            hello.append(commandLine.getOptionValue("more"));
        }
        System.out.println(hello.toString());
    }
}
