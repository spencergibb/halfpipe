package thirtytwo.degrees.halfpipe.example.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
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
public class HelloCommand extends Command {

    @Inject
    Sayer sayer;

    protected HelloCommand() {
        super("hello", "Say hello to the command line");
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
