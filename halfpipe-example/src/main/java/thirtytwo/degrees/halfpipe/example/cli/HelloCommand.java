package thirtytwo.degrees.halfpipe.example.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.*;
import org.springframework.stereotype.Component;
import thirtytwo.degrees.halfpipe.example.resources.Sayer;

import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 10:07 PM
 */
@Component
public class HelloCommand implements CommandMarker {

    @Inject
    Sayer sayer;

    @CliAvailabilityIndicator({"hello"})
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = "hello", help = "Say hello to the shell")
    public String hello(
            @CliOption(key = "message", mandatory = true, help = "The hello world message")
            String message,

            @CliOption(key = "more", mandatory = false,
                    help = "Have some more to say while saying hello",
                    specifiedDefaultValue="")
            String more,

            @CliOption(key = {"", "config"}, mandatory = false, help = "config file")
            String config )
    {
        StringBuilder hello = new StringBuilder(sayer.hello());
        hello.append(" with this message, "+message);
        if (!StringUtils.isBlank(more)) {
            hello.append(" "+more);
        }
        //System.out.println("\n\n\n\n"+hello+"\n\n\n\n");
        return hello.toString();
    }
}
